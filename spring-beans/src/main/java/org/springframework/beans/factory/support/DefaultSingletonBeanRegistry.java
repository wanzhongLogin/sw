package org.springframework.beans.factory.support;

import com.sun.istack.internal.Nullable;
import org.springframework.beans.factory.*;
import org.springframework.core.SimpleAliasRegistry;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 对接口SingletonBeanRegistry的实现
 *
 * 共享bean实例的通用注册表，实现SingletonBeanRegistry。
 * 允许通过bean名称注册应该为注册表的所有调用者共享的单例实例。
 * 还支持在关闭注册表时销毁DisposableBean实例（可能对应于已注册的单例，
 * 也可能不对应注册的单例）。 可以注册bean之间的依赖关系以强制执行适当的关闭顺序。
 *
 * 该类主要用作BeanFactory实现的基类，分解了单例bean实例的通用管理。
 * 请注意，ConfigurableBeanFactory接口扩展了SingletonBeanRegistry接口。
 *
 * 请注意，与AbstractBeanFactory和DefaultListableBeanFactory（继承自它）相比，
 * 此类既不假定bean定义概念也不假定bean实例的特定创建过程。
 * 或者也可以用作委托的嵌套助手。
 */
public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {

    /**
     * cachee of singleton objects:bean name to bean instarnce
     * 缓存singleton实例:beanName-实例
     */
    private final Map<String,Object> singletonObjects = new ConcurrentHashMap<>(256);

    /**
     * set of registered singletons,containing the bean name in registration order
     * 已经注册过的bean
     */
    private final Set<String> registeredSingletons = new LinkedHashSet<>(256);

    /**
     * cache of singleton factories:bean name to objectFactory
     * 单利工厂缓存
     */
    private final Map<String,ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    /**
     * cache of early singleton objects
     * 早期单利对象的缓存
     */
    private final Map<String,Object> earlySingletonObjects = new HashMap<>(16);

    /**
     * names of beans that are currently in creation
     * 当前正在创建的bean
     */
    private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    /**
     * names of beans currently excluded from in creation checks
     * 当前从创建检查中排除的bean的名称
     * bean的名字目前在创作检查排除
     */
    private final Set<String> inCreationCheckExclusions = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    /**被抑制的异常列表，可用于关联相关原因*/
    @Nullable
    private Set<Exception> suppressedExceptions;

    /**
     * 标志指示我们当前是否在destroySingletons中的标志
     */
    private boolean singletonsCurrentlyInDestruction = false;

    /**
     * 一次性bean实例：bean名称为一次性实例。
     * 该map中所装在的是摧毁当前对应的bean所需要的DisposableBean类
     */
    private final Map<String,Object> disposableBeans = new LinkedHashMap<>();

    //包含bean名称之间的映射：bean name到bean包含的bean名称集
    private final Map<String,Set<String>> containedBeanMap = new ConcurrentHashMap<>(16);

    /**bean所依赖的bean*/
    private final Map<String,Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);


    private final Map<String,Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);

    //Register the given existing object as singleton in the bean registry,under the given bean name.
    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        Assert.notNull(beanName,"bean name must not be null");
        Assert.notNull(singletonObject,"singleton object must not be null");
        synchronized (this.singletonObjects){
            //获取已经注册过的bean
            Object oldObject = this.singletonObjects.get(beanName);
            if(oldObject != null){
                throw new IllegalStateException("could not register object["+singletonObject+"]under bean name'"+
                        beanName+"':there is already object["+oldObject+"]bound");
            }
            addSingleton(beanName,singletonObject);
        }
    }

    //Add the given singleton object to the singleton cache of this factory
    protected void addSingleton(String beanName,Object singletonObject){
        synchronized (this.singletonObjects){
            //放入缓存bean
            this.singletonObjects.put(beanName,singletonObject);
            //将该bean所对应的bean工厂移除
            this.singletonFactories.remove(beanName);
            //将缓存到该还没有注册的bean移除
            this.earlySingletonObjects.remove(beanName);
            //将已经注册了的bean的名称加入到该缓存中
            this.registeredSingletons.add(beanName);
        }
    }

    /**
     * add the given singleton factory forbuilding the specified singleton if necessary
     * 如有必要，添加给定的单件工厂以构建指定的单例。
     * @param beanName
     * @param singletonFactory
     */
    protected void addSingletonFactory(String beanName,ObjectFactory<?> singletonFactory){
        Assert.notNull(singletonFactory,"singleton factory must not be nulll");
        synchronized (this.singletonObjects){
            if(!this.singletonObjects.containsKey(beanName)){
                this.singletonFactories.put(beanName,singletonFactory);
                this.earlySingletonObjects.remove(beanName);
                this.registeredSingletons.add(beanName);
            }
        }
    }

    @Override
    @Nullable
    public Object getSingleton(String beanName) {
        return this.getSingleton(beanName,true);
    }

    /**
     * 获取单利bean
     * @param beanName
     * @param allowEarlyReference 是否应该创建早期参考
     * @return
     */
    @Nullable
    public Object getSingleton(String beanName,boolean allowEarlyReference){

        Object singletonObject = this.singletonObjects.get(beanName);

        //如果当前bean为空,或者正在创建中
        if(singletonObjects == null && isSingletonCurrentlyInCreation(beanName)){
            synchronized (this.singletonObjects){
                //获取早期的单利对象
                singletonObject = this.earlySingletonObjects.get(beanName);

                //如果还是为空
                if(singletonObjects == null && allowEarlyReference){
                    ObjectFactory<?> objectFactory = this.singletonFactories.get(beanName);
                    if(objectFactory != null){
                        //创建bean
                        singletonObject = objectFactory.getObject();
                        this.earlySingletonObjects.put(beanName,singletonObject);
                        this.singletonFactories.remove(beanName);
                    }
                }   
            }
        }
        return singletonObject;
    }

    //获取bean
    public Object getSingleton(String beanName,ObjectFactory<?> singletonFactory){
        Assert.notNull(beanName,"bean name must not be null");

        synchronized (this.singletonObjects){

            //根据beanName获取数据
            Object singletonObject = this.singletonObjects.get(beanName);

            if(singletonObject == null){

                if(this.singletonsCurrentlyInDestruction){
                    throw new BeanCreationNotAllowedException(beanName,"当这个工厂的单例处于破坏状态时不允许使用单例bean创建" +
                            "(不要在destroy方法实现中从BeanFactory请求bean）");
                }

                //校验当前bean是否在创建名单之外,并且将该beanName加入到创建缓存中
                beforeSingletonCreation(beanName);

                boolean newSingleton = false;
                boolean recordSuppressedException = this.suppressedExceptions == null;
                if(recordSuppressedException){
                    this.suppressedExceptions = new LinkedHashSet<>();
                }
                try{
                    singletonObject = singletonFactory.getObject();
                    newSingleton = true;
                }catch(IllegalStateException e){
                    singletonObject = this.singletonObjects.get(beanName);
                    if(singletonObject == null){
                        throw e;
                    }
                }catch (BeanCreationException e){
                    if(recordSuppressedException){
                        for (Exception suppressedException : this.suppressedExceptions) {
                            e.addRelatedCause(suppressedException);
                        }
                    }
                    throw e;
                }finally {
                    if(recordSuppressedException){
                        this.suppressedExceptions = null;
                    }
                    //TODO 检查该bean是否正在销毁的数据 并且 移除在当前正在创建的bean的缓存map
                    afterSingletonCreation(beanName);
                }
                if(newSingleton){
                    addSingleton(beanName,singletonObject);
                }
            }
            return singletonObject;
        }
    }

    /**
     * 注册在创建单例bean实例期间碰巧被抑制的异常。 例如 临时循环参考分辨率问题。
     * @param ex
     */
    protected void onSuppressedException(Exception ex){
        synchronized (this.singletonObjects){
            if(this.suppressedExceptions != null){
                this.suppressedExceptions.add(ex);
            }
        }
    }

    /**
     * 移除bean name的实例
     * @param beanName
     */
    protected void removeSingleton(String beanName){
        synchronized (this.singletonObjects){
            this.singletonObjects.remove(beanName);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.remove(beanName);
        }
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames(){
        synchronized (this.singletonObjects){
            return StringUtils.toStringArray(this.registeredSingletons);
        }
    }
    @Override
    public int getSingletonCount() {
        synchronized (this.singletonObjects){
            return this.registeredSingletons.size();
        }
    }

    public void setCurrentlyInCreation(String beanName,boolean inCreation){
        Assert.notNull(beanName,"bean name must not be null");
        if(!inCreation){
            this.inCreationCheckExclusions.add(beanName);
        }else{
            this.inCreationCheckExclusions.remove(beanName);
        }
    }

    public boolean isCurrentlyInCreation(String beanName){
        Assert.notNull(beanName,"bean name must not be null");
        return (!this.inCreationCheckExclusions.contains(beanName) &&
        this.isActuallyInCreation(beanName));
    }

    protected boolean isActuallyInCreation(String beanName){
        return isSingletonCurrentlyInCreation(beanName);
    }

    //返回指定的单例bean当前是否正在创建
    public boolean isSingletonCurrentlyInCreation(String beanName){
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    //callback after singleton creation
    protected void beforeSingletonCreation(String beanName){
        if(!this.inCreationCheckExclusions.contains(beanName)&& !this.singletonsCurrentlyInCreation.add(beanName)){
            throw new BeanCurrentlyInCreationException(beanName);
        }
    }

    //Callback after singleton creation
    protected void afterSingletonCreation(String beanName){
        if(!this.inCreationCheckExclusions.contains(beanName) && !this.singletonsCurrentlyInCreation.remove(beanName)){
            throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
        }
    }

    /**
     * 将给定的bean添加到此注册表中的一次性Bean列表中
     */
    public void registerDisposableBean(String beanName,DisposableBean bean){
        synchronized (this.disposableBeans){
            this.disposableBeans.put(beanName,bean);
        }
    }

    public void registerContainedBean(String containedBeanName,String containingBeanName){
        synchronized (this.containedBeanMap){
            Set<String> strings = this.containedBeanMap.
                    computeIfAbsent(containedBeanName, k -> new LinkedHashSet<>(8));
            if(!strings.add(containedBeanName)){
                return;
            }
            this.registerDependentBean(containedBeanName,containingBeanName);
        }
    }

    /**
     * 为给予的bean注册一个依赖bean
     */
    public void registerDependentBean(String beanName,String dependentBeanName){
        String cononicalName = canonicalName(beanName);
        synchronized (this.dependentBeanMap){
            Set<String> strings = this.dependentBeanMap.computeIfAbsent(cononicalName, k -> new LinkedHashSet<>(8));
            if(!strings.add(dependentBeanName)){
                return;
            }
            synchronized (this.dependenciesForBeanMap){
                Set<String> strings1 = this.dependenciesForBeanMap.computeIfAbsent(dependentBeanName, k -> new LinkedHashSet<>(8));
                strings1.add(cononicalName);
            }
        }
    }

    /**
     * 确定指定的依赖bean是否已注册为依赖于给定bean或其任何传递依赖项。
     * @param beanName
     * @param dependentBeanName
     * @return
     */
    protected boolean isDependent(String beanName,String dependentBeanName){
        synchronized (this.dependenciesForBeanMap){
            return this.isDependent(beanName,dependentBeanName,null);
        }
    }

    protected boolean isDependent(String beanName,String dependentBeanName,@Nullable Set<String> alreadySeen){
        if(alreadySeen != null && alreadySeen.contains(beanName)){
            return false;
        }
        String canonicalName = canonicalName(beanName);
        Set<String> dependentBeans = this.dependentBeanMap.get(canonicalName);
        if(dependentBeans == null){
            return false;
        }
        if(dependentBeans.contains(dependentBeanName)){
            return true;
        }
        for (String dependentBean : dependentBeans) {
            if(alreadySeen == null){
                alreadySeen = new HashSet<>();
            }
            alreadySeen.add(beanName);
            if(isDependent(dependentBean,dependentBeanName,alreadySeen)){
                return false;
            }
        }
        return false;
    }

    /**
     * 确定是否已为给定名称注册了依赖bean。
     */
    protected boolean hasDependentBean(String beanName){
        return this.dependentBeanMap.containsKey(beanName);
    }

    /**
     * 返回给定的bean所有的依赖bean
     */
    public String[] getDependentBeans(String beanName){
        Set<String> strings = this.dependentBeanMap.get(beanName);
        if(strings == null){
            return new String[0];
        }
        synchronized (this.dependentBeanMap){
            return StringUtils.toStringArray(strings);
        }
    }

    /**
     * 返回指定bean所依赖的所有bean的名称（如果有）。
     */
    public String[] getDependenciesForBean(String beanName){
        Set<String> strings = this.dependenciesForBeanMap.get(beanName);
        if(strings == null){
            return new String[0];
        }
        synchronized (this.dependenciesForBeanMap){
            return StringUtils.toStringArray(strings);
        }
    }

    public void destroySingletons(){
        synchronized (this.singletonObjects){
            this.singletonsCurrentlyInDestruction = true;
        }
        String[] disposableBeanNames;
        synchronized (this.disposableBeans){
            disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet());
        }
        for (int i = disposableBeanNames.length - 1;i>= 0;i--){
            //销毁bean
            destroySingleton(disposableBeanNames[i]);
        }
        this.containedBeanMap.clear();;
        this.dependentBeanMap.clear();;
        this.dependenciesForBeanMap.clear();

        this.clearSingletonCache();
    }

    /**
     * 当前类清除缓存
     */
    protected void clearSingletonCache(){
        synchronized (this.singletonObjects){
            this.singletonObjects.clear();
            this.singletonFactories.clear();
            this.earlySingletonObjects.clear();
            this.registeredSingletons.clear();
            this.singletonsCurrentlyInDestruction = false;
        }
    }

    /**
     * 销毁bean
     * @param beanName
     */
    public void destroySingleton(String beanName){

        //移除bean
        removeSingleton(beanName);

        DisposableBean disposableBean;
        synchronized (this.disposableBeans){
            disposableBean = (DisposableBean) this.disposableBeans.remove(beanName);
        }
        destroyBean(beanName,disposableBean);
    }

    /**
     * 销毁bean，在销毁之前需要销毁所有跟它有依赖关系的bean
     */
    protected void destroyBean(String beanName,@Nullable DisposableBean bean){

        //获取到当前bean name所依赖的bean
        Set<String> dependencies;
        synchronized (this.dependentBeanMap){
            dependencies = this.dependenciesForBeanMap.remove(beanName);
        }
        if(dependencies != null){
            //然后依次进行销毁
            for (String dependency : dependencies) {
                destroySingleton(dependency);
            }
        }

        //在这里才是进行真正的销毁,用DisposableBean
        if(bean != null){
            try{
                bean.destroy();
            }catch(Exception e){
                logger.error("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }

        Set<String> containedBeans;
        synchronized (this.containedBeanMap){
            containedBeans = this.containedBeanMap.remove(beanName);
        }
        if(containedBeans != null){
            for (String containedBean : containedBeans) {
                destroySingleton(containedBean);
            }
        }

        synchronized (this.dependentBeanMap){
            for(Iterator<Map.Entry<String, Set<String>>> it = this.dependentBeanMap.entrySet().iterator();it.hasNext();){
                Map.Entry<String, Set<String>> next = it.next();
                Set<String> value = next.getValue();
                value.remove(beanName);
                if(value.isEmpty()){
                    it.remove();
                }
            }
        }

        this.dependenciesForBeanMap.remove(beanName);
    }

    @Override
    public Object getSingletonMutex() {
        return this.singletonObjects;
    }

}
