package org.springframework.core;

/**
 * 用于管理别名的公共接口。作为BeanDefinitionRegistry的超级接口
 */
public interface AliasRegistry {

    /**
     * 将给定的名字,注册成别名
     */
    void registerAlias(String name,String alias);

    /**
     * 移除别名
     */
    void removeAlias(String alias);

    /**
     * 查看是否注册过别名
     */
    boolean isAlias(String name);

    /**
     * 返回该别名所有的注册过的别名
     */
    String[] getAliases(String name);
}
