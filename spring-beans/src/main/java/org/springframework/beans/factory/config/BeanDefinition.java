package org.springframework.beans.factory.config;

import com.sun.istack.internal.Nullable;
import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;

/**
 * 该bean定义是包含一个bean标签的内容,描述了一个bean的实例内容所有的property values
 *
 */
public interface BeanDefinition extends AttributeAccessor,BeanMetadataElement {

    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


    int ROLE_APPLICATION = 0;

    int ROLE_SUPPORT = 1;

    int ROLE_INFRASTRUCTURE = 2;

    /**
     * 如果存在父Bean定义,那么为父bean定义设置名称
     * @param parentName
     */
    void setParentName(@Nullable String parentName);

    /**
     * 获取父beanDefinition的名称
     * @return
     */
    @Nullable
    String getParentName();

    /**
     * Override the bean class name of this bean definition
     */
    void setBeanClassName(@Nullable String beanClassName);

    /**
     * get current bean definition's bean class name
     * @return
     */
    @Nullable
    String getBeanClassName();

    /**
     * Override the target scope of this bean, specifying a new scope name.
     * @param scope
     */
    void setScope(@Nullable String scope);

    /**
     * return current bean scope of the bean definition;
     * Override the target scope of this bean, specifying a new scope name.
     * @return
     */
    @Nullable
    String getScope();

    /**
     * 设置是否应该懒惰地初始化此bean。
     * @param lazyInit
     */
    void setLazyInit(boolean lazyInit);

    /**
     * 是否是懒加载的bean
     * @return
     */
    boolean isLazyInit();

    /**
     * 设置当前bean所依赖的bean
     * @param dependsOn
     */
    void setDependsOn(@Nullable String... dependsOn);

    /**
     * 返回当前bean所依赖的所有Bean的名称
     * @return
     */
    @Nullable
    String[] getDependsOn();

    /**
     * 设置此bean是否可以自动连接到其他bean。
     * @param autowireCandidate
     */
    void setAutowireCandidate(boolean autowireCandidate);

    /**
     * 是否自动连接到其他bean
     * @return
     */
    boolean isAutowireCandidate();

    /**
     * 设置此bean是否为主要autowire候选者
     * Set whether this bean is a primary autowire candidat
     * @param primary
     */
    void setPrimary(boolean primary);
    boolean isPrimary();

    /**
     * specify the factory bean to use if any
     * 指定bean工厂来使用,如果有
     * @param factoryBeanName
     */
    void setFactoryBeanName(@Nullable String factoryBeanName);

    @Nullable
    String getFactoryBeanName();

    /**
     * 指定工厂方法（如果有）。 将使用构造函数参数调用此方法，如果未指定任何参数，
     * 则不使用参数调用此方法。 该方法将在指定的工厂bean（如果有）上调用，
     * 或者作为本地bean类的静态方法调用。
     * @param factoryMethodName
     */
    void setFactoryMethodName(@Nullable String factoryMethodName);

    @Nullable
    String getFactoryMethodName();

    /**
     * 返回此bean的构造函数参数值。
     * 可以在bean工厂后处理期间修改返回的实例。
     * @return
     */
    ConstructorArgumentValues getConstructorArgumentValues();

    /**
     * Return if there are constructor argument values defined for this bean.
     * @return
     */
    default boolean hasConstructorArgumentValues(){
        return !getConstructorArgumentValues().isEmpty();
    }

    /**
     * 返回要应用于bean的新实例的属性值。
     * 可以在bean工厂后处理期间修改返回的实例。
     * @return
     */
    MutablePropertyValues getPropertyValues();

    /**
     * Return if there are property values values defined for this bean
     * @return
     */
    default boolean hasPropertyValues(){
        return !getPropertyValues().isEmpty();
    }

    /**
     * 是否是singleton
     * @return
     */
    boolean isSingleton();

    boolean isPrototype();

    /**
     * Return whether this bean is "abstract", that is,
     * not meant to be instantiated.
     * @return
     */
    boolean isAbstract();

    int getRole();

    /**
     * 获取该Bean definiton的描述
     * @return
     */
    @Nullable
    String getDescription();

    /**
     * Return a description of the resource that this bean definition
     * came from (for the purpose of showing context in case of errors).
     * @return
     */
    @Nullable
    String getResourceDescription();

    /**
     * 返回原始BeanDefinition，如果没有则返回null。 允许检索修饰的bean定义（如果有）。
     * 请注意，此方法返回直接发起者。 遍历创建者链以查找用户定义的原始BeanDefinition。
     * @return
     */
    @Nullable
    BeanDefinition getOriginatingBeanDefinition();
}
