package org.springframework.beans.factory.config;

import com.sun.istack.internal.Nullable;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;

import java.util.Set;

/**
 * 提供创建bean,自动注入,初始化以及应用bean的后处理器
 *
 *
 * BeanFactory接口的扩展由能够自动装配的bean工厂实现，前提是它们希望为现有bean实例公开此功能。
 * BeanFactory的这个子接口并不适用于普通的应用程序代码：对于典型的用例，
 * 坚持使用BeanFactory或ListableBeanFactory。
 *
 * 其他框架的集成代码可以利用此接口来连接和填充Spring无法控制其生命周期的现有Bean实例。
 * 例如，这对WebWork Actions和Tapestry Page对象特别有用。
 *
 * 请注意，此接口不是由ApplicationContext外观实现的，因为应用程序代码几乎不使用它。
 * 也就是说，它也可以从应用程序上下文中获得，可以通过ApplicationContext的
 * ApplicationContext.getAutowireCapableBeanFactory（）方法访问。
 *
 * 您还可以实现BeanFactoryAware接口，该接口即使在ApplicationContext中运行时也会公开内部
 * BeanFactory，以访问AutowireCapableBeanFactory：只需将传入的BeanFactory强制转换为
 * AutowireCapableBeanFactory。
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    int AUTOWIRE_NO = 0;

    int AUTOWIRE_BY_NAME = 1;

    int AUTOWIRE_BY_TYPE = 2;

    int AUTOWIRE_CONSTRUCTOR = 3;

    @Deprecated
    int AUTOWIRE_AUTODETECT = 4;

    /**
     * 创建Bean
     *
     * 完全创建给定类的新bean实例。
     */
    <T> T createBean(Class<T> beanClass) throws BeansException;

    <T> T createBean(Class<?> beanClass,int autowireMode,boolean dependencyCheck) throws BeansException;

    /**
     * 通过应用after-instantiation回调填充给定的bean实例
     * 和bean属性后处理（例如，用于注释驱动的注入）。
     */
    void autowireBean(Object existingBean) throws BeansException;


    Object configureBean(Object existingBean,String beanName) throws BeansException;

    Object autowire(Class<?> beanClass,int autowireMode,boolean dependencyCheck) throws BeansException;

    void autowireBeanProperties(Object existingBean,int autowireMode,boolean dependencyCheck) throws BeansException;

    void applyBeanPropertyValues(Object existingBean,String beanName) throws BeansException;

    Object initializeBean(Object existingBean,String beanName) throws BeansException;

    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean,String beanName) throws BeansException;

    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException;

    /**
     * 销毁bean
     */
    void destroyBean(Object existingBean);

    <T> NamedBeanHolder<T> resolveNamedBean(Class<T> requiredType) throws BeansException;

    Object resolveDependency(DependencyDescriptor descriptor,@Nullable String requestingBeanName) throws BeansException;

    Object resolveDependency(DependencyDescriptor descriptor, @Nullable String requestingBeanName,@Nullable Set<String> autowiredBeanNames,@Nullable TypeConverter typeConverter) throws BeansException;
}
