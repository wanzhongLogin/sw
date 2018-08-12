package org.springframework.beans.factory;

import com.sun.istack.internal.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;

/**
 * 用户获取bean的factory
 */
public interface BeanFactory {


    String FACTORY_BEAN_PREFIX = "&";


    /**
     * 通过name来获取bean
     */
    Object getBean(String name) throws BeansException;


    /**
     * 通过name + bean的类型来获取bean
     */
    <T> T getBean(String name,@Nullable Class<T> requiredType) throws BeansException;

    /**
     * 通过name + 参数
     * @param name
     * @param args
     * @return
     * @throws BeansException
     */
    Object getBean(String name,Object... args) throws BeansException;

    /**
     * 通过bean的class类型来获取
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;


    <T> T getBean(Class<T> requiredType,Object... args) throws BeansException;

    /**
     * 判断bean是否存在
     */
    boolean containsBean(String name);

    /**
     * 该bean是否是singleton
     */
    boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

    /**
     * 该bean是否是propotype
     */
    boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

    /**
     * 检查给定的bean是否与指定类型匹配
     */
    boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException;


    boolean isTypeMatch(String name,@Nullable Class<?> typeToMatch) throws NoSuchBeanDefinitionException;

    /**
     * 返回Bean的类型
     */
    @Nullable
    Class<?> getType(String name) throws NoSuchBeanDefinitionException;

    /**
     * 获取bean的别名
     */
    String[] getAliases(String name);
}
