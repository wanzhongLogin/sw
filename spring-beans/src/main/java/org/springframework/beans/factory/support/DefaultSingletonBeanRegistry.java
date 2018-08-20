package org.springframework.beans.factory.support;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.SingletonBeanRegistry;
import org.springframework.core.SimpleAliasRegistry;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
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
     * cache of singleton factories:bean name to objectFactory
     */
    private final Map<String,ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    /**
     * cache of early singleton objects
     */
    private final Map<String,Object> earlySingletonObjects = new HashMap<>(16);

    /**
     * set of registered singletons,containing the bean name in registration order
     */
    private final Set<String> registeredSingletons = new LinkedHashSet<>(256);

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {

    }

    @Override
    public Object getSingleton(String beanName) {
        return null;
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return false;
    }

    @Override
    public int getSingletonCount() {
        return 0;
    }

    @Override
    public Object getSingletonMetex() {
        return null;
    }
}
