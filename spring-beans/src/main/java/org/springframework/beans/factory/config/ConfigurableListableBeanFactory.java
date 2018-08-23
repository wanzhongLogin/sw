package org.springframework.beans.factory.config;

import org.springframework.beans.factory.ListableBeanFactory;

/**
 * beanfactory的配置清单,指定忽略类型及接口等
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory,AutowireCapableBeanFactory,ConfigurableBeanFactory {
}
