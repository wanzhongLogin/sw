package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.AliasRegistry;

/**
 * 包含bean定义的注册表的接口，例如RootBeanDefinition和ChildBeanDefinition实例。
 * 通常由BeanFactories实现，BeanFactories内部使用AbstractBeanDefinition层次结构。
 * 这是Spring的bean工厂包中唯一封装bean定义注册的接口。
 * 标准BeanFactory接口仅涵盖对完全配置的工厂实例的访问。
 *
 * Spring的bean定义读者期望在这个接口的实现上工作。
 * Spring核心中的已知实现者是DefaultListableBeanFactory和GenericApplicationContext。
 */
public interface BeanDefinitionRegistry extends AliasRegistry {

    /**
     * 注册一个新的bean实例
     * @param beanName
     * @param beanDefinition
     * @throws BeanDefinitionStoreException
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException;

    /**
     * 移除Bean
     * @param beanName
     * @throws NoSuchBeanDefinitionException
     */
    void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * 获取bean定义
     */
    BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * 是否包含了改Bean
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 获取所有的bean definion的名字
     * @return
     */
    String[] getBeanDefinitionNames();

    /**
     * 该bean是否在被使用
     */
    boolean isBeanNameInUse(String beanName);
}
