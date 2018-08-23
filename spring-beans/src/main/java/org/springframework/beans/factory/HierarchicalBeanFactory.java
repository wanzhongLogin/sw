package org.springframework.beans.factory;


import com.sun.istack.internal.Nullable;

/**
 * 可以被作为分层结构中的一部分的bean工厂实现
 *
 * 集成beanFactory,并且在此功能上,增加了对parentFactory的支持
 */
public interface HierarchicalBeanFactory  extends BeanFactory{

    /**
     * return the parent bean factory or if there is none
     * 返回父类的bean工厂,如果没有,返回Null
     */
    @Nullable
    BeanFactory getParentBeanFactory();

    /**
     *
     * @param name
     * @return
     */
    boolean containsLocation(String name);
}
