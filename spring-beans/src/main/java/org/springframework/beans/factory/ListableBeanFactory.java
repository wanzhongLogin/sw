package org.springframework.beans.factory;

import com.sun.istack.internal.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 对接口beanFactory的扩展
 *
 * 是BeanFactory接口的扩展，被能够一次性列举所有它们bean实例，而不是试图根据客户端请求一个一个的通过名字查找的
 * 的工厂容器实现。那些需要预先加载所有bean定义的工厂需要实现这个接口。
 *
 * 提供了批量获取Bean的方法
 */
public interface ListableBeanFactory extends BeanFactory{

    /**
     * 检查bean工厂中,是否有该bean
     * @param beanName
     * @return
     */
    boolean containsBeanDefinition(String beanName);


    /**
     * 获取beans的总数
     */
    int getBeanDefinitionCount();


    /**
     * 获取所有bean的名称
     * @return
     */
    String[] getBeanDefinitionNames();


    /**
     * 返回匹配给定类型（包括子类）的所有bean的名字，如果是普通bean，则是bean定义的名字，如果是
     * FactoryBean，则是其getObjectType方法返回的对象的名字
     * 跟据bean 的类型获取bean .
     * 它不会检查嵌套的FactoryBean创建的bean
     */
    String[] getBeanNamesForType(ResolvableType type);

    /**
     * 根据类来获取bean的名称
     */
    String[] getBeanNamesForType(@Nullable Class<?> type);


    String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit);


    <T> Map<String, T> getBeansOfType(@Nullable Class<T> type) throws BeansException;

    <T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException;


    /**
     * 通过指定的注解类型，获取所有那些还没有创建bean实例的名字。
     */
    String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType);

    /**
     * 查找所有注解为指定类型的bean，返回一个bean名字与其对应实例的映射表
     */
    Map<String,Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException;


    /**
     * 查找指定bean的注解。
     * 如果在指定bean自身上面没有找到，则遍历它实现的接口和他的超类。
     */
    @Nullable
    <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
            throws NoSuchBeanDefinitionException;









}
