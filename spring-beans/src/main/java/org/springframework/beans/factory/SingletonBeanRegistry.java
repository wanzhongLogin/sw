package org.springframework.beans.factory;

import com.sun.istack.internal.Nullable;

/**
 * SingletonBeanRegistry
 * 定义了共享bean实例的注册接
 * 定义对单利bean的注册和获取
 */
public interface SingletonBeanRegistry {

    /**
     * Register the given existing object as singleton in the bean registry,
     * under the given bean name.
     * 将规定的beanName和单态注册
     * 在容器中创建一个指定的单利bean的类
     */
    void registerSingleton(String beanName,Object singletonObject);

    /**
     * 返回一个单利类
     */
    @Nullable
    Object getSingleton(String beanName);


    /**
     * 判断容器中是否包含该单利类
     */
    boolean containsSingleton(String beanName);

    /**
     * 获取容器中单利类的个数
     */
    int getSingletonCount();

    /**
     * 获取所有注册的单利bean的名称
     * @return
     */
    String[] getSingletonNames();

    /**
     * return the singleton mutex uesed by this registry
     */
    Object getSingletonMutex();
}
