package org.springframework.beans.factory.support;


import com.sun.istack.internal.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;

import java.security.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在defaultSingletonBeanRegistry的基础上增加了对facotryBean的特殊处理
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{

    /**
     * cache of singleton objects created by factoryBeans,factoryBean name - object
     */
    private final Map<String,Object> factoryBeanObjectCache = new ConcurrentHashMap<>(16);

    /**
     * 确定给定FactoryBean的类型
     */
    @Nullable
    protected Class<?> getTypeForFactoryBean(final FactoryBean<?> factoryBean){
        try{
            //获取系统安全接口
            if(System.getSecurityManager() != null){
                PrivilegedAction<Class<?>> getObjectType = factoryBean::getObjectType;
                return AccessController.doPrivileged(getObjectType,getAccessControlContext());
            }else{
                return factoryBean.getObjectType();
            }
        }catch(Exception e){
            return null;
        }
    }

    /**
     * 获取要从给定FactoryBean公开的对象（如果在缓存形式中可用）。 快速检查最小同步。
     */
    protected Object getCacheObjectForFactoryBean(String beanName){
        return this.factoryBeanObjectCache.get(beanName);
    }

    /**
     * 获取要从给定FactoryBean公开的对象
     * @param factoryBean
     * @param beanName
     * @param shouldPostProcess
     * @return
     */
    protected Object getObjectFromFactoryBean(FactoryBean<?> factoryBean,String beanName,boolean shouldPostProcess){
        //当前bean是单利,并且要在单利缓存里面
        if(factoryBean.isSingleton() && containsSingleton(beanName)){
            //获取父类缓存singlton bean的map进行加锁
            synchronized (super.getSingletonMutex()){
                Object object = this.factoryBeanObjectCache.get(beanName);
                if(object == null){

                    object = this.doGetObjectFromFactoryBean(factoryBean,beanName);

                    //当前缓存的factory
                    Object alreadyThere = this.factoryBeanObjectCache.get(beanName);

                    if(alreadyThere != null){
                        object = alreadyThere;
                    }else{
                        if(shouldPostProcess){
                            //返回指定的单例bean当前是否正在创建
                            if(isSingletonCurrentlyInCreation(beanName)){
                                return object;
                            }
                            //校验当前bean是否在创建名单之外,并且将该beanName加入到创建缓存中
                            beforeSingletonCreation(beanName);

                            try{
                                object = this.postProcessObjectFromFactoryBean(object,beanName);
                            }catch(Exception e){
                                throw new BeanCreationException("");
                            }finally {
                                //检查该bean是否正在销毁的数据 并且 移除在当前正在创建的bean的缓存map
                                afterSingletonCreation(beanName);
                            }
                            //当前bean对象是否在单利bean缓存中
                            if(containsSingleton(beanName)){
                                //将该beanname - factory放入缓存
                                this.factoryBeanObjectCache.put(beanName,object);
                            }
                        }
                    }
                }
                return object;
            }
        }else{
            Object object = doGetObjectFromFactoryBean(factoryBean, beanName);
            if(shouldPostProcess){
                try{
                    object = postProcessObjectFromFactoryBean(object,beanName);
                }catch(Throwable e){
                    throw new BeanCreationException(beanName,"");
                }
            }
            return object;
        }
    }

    /**
     * 获取要从给定FactoryBean公开的对象
     */
    private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory,final String beanName) throws BeanCreationException {
        Object object = null;
        try{
            //检查是否开启了类安全检查
            if(System.getSecurityManager() != null){
                AccessControlContext acc = getAccessControlContext();
                try{
                    //通过工厂获取bean 对象
                    object = AccessController.doPrivileged((PrivilegedExceptionAction<Object>) factory::getObject, acc);
                }catch(PrivilegedActionException pae){
                    throw pae.getException();
                }
            }
        }catch(FactoryBeanNotInitializedException e){
            throw new BeanCurrentlyInCreationException(beanName,e.toString());
        }catch (Throwable ex){
            throw new BeanCreationException(beanName,"");
        }
        //上面没有获取到
        if(object == null){
            //判断当前bean是否正在处于创建中
            if(isSingletonCurrentlyInCreation(beanName)){
                throw new BeanCurrentlyInCreationException(beanName,"");
            }
            //返回一个空bean
            object = new NullBean();
        }
        return object;
    }

    /**
     * 对从FactoryBean获取的给定对象进行后处理。 生成的对象将暴露给bean引用。
     * 默认实现只是按原样返回给定的对象。 子类可以覆盖它，例如，应用后处理器。
     * @return
     */
    protected Object postProcessObjectFromFactoryBean(Object obj,String beanName){
        return obj;
    }

    /**
     * 如果可能，为给定的bean获取FactoryBean。
     * @param beanName
     * @param beanInstance
     * @return
     * @throws BeansException
     */
    protected FactoryBean<?> getFactoryBean(String beanName,Object beanInstance) throws BeansException {
        if(!(beanInstance instanceof FactoryBean)){
            throw new BeanCreationException("");
        }
        return (FactoryBean<?>) beanInstance;
    }

    /**
     * 重写以清除FactoryBean对象缓存。
     */
    @Override
    protected void removeSingleton(String beanName){
        synchronized (getSingletonMutex()){
            super.clearSingletonCache();
            this.factoryBeanObjectCache.clear();
        }
    }

    /**
     * Return the security context for this bean factory. If a security manager
     * is set, interaction with the user code will be executed using the privileged
     * of the security context returned by this method.
     * @see AccessController#getContext()
     */
    protected AccessControlContext getAccessControlContext() {
        return AccessController.getContext();
    }
}
