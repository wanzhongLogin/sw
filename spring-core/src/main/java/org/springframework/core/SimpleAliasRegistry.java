package org.springframework.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * aliasRegistry的默认实现
 * 主要使用map作为alias缓存,并实现接口aliasRegistry
 */
public class SimpleAliasRegistry implements AliasRegistry{

    protected final Log logger = LogFactory.getLog(getClass());

    /** map from alias to canonical name */
    private final Map<String,String> aliasMap = new ConcurrentHashMap<>(16);


    @Override
    public void registerAlias(String name, String alias) {
        Assert.hasText(name,"'name' must not be empty");
        Assert.hasText(alias,"'alias' must not be empty");

        synchronized (this.aliasMap){
            //如果别名和bean的名字相同
            if(alias.equals(name)){
                //将从aliasMap中移除
                this.aliasMap.remove(alias);
                if(logger.isDebugEnabled()){
                    logger.debug("alias definiton '" + alias +"ignored since it proints to same name");
                }
            }else{
                //获取注册的别名?
                String registeredName = this.aliasMap.get(alias);
                if(registeredName != null){
                    if(registeredName.equals(name)){
                        return;
                    }
                }
                //不允许覆盖别名
                if(!allowAliasOverriding()){
                    throw new IllegalStateException("Cannot define alias '" + alias + "' for name '" +
                            name + "': It is already registered for name '" + registeredName + "'.");
                }
                if (logger.isInfoEnabled()) {
                    logger.info("Overriding alias '" + alias + "' definition for registered name '" +
                            registeredName + "' with new target name '" + name + "'");
                }
                //检查别名是否可以进行注册
                checkForAliasCircle(name,alias);
                //放入map中
                this.aliasMap.put(alias,name);
                if(logger.isDebugEnabled()){
                    logger.debug("Alias definition '" + alias + "' registered for name '" + name + "'");
                }
            }
        }

    }

    /**
     * 返回是否允许覆盖别名
     * @return
     */
    protected boolean allowAliasOverriding(){
        return true;
    }

    public boolean hasAlias(String name,String alias){
        for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
            String value = entry.getValue();
            if(value.equals(name)){
                final String registeredAlias = entry.getKey();
                return (registeredAlias.equals(alias) || hasAlias(registeredAlias, alias));
            }
        }
        return false;
    }

    @Override
    public void removeAlias(String alias) {
        synchronized (this.aliasMap){
            String remove = this.aliasMap.remove(alias);
            if(remove == null){
                throw new IllegalStateException("不存在别名:"+alias);
            }
        }
    }

    @Override
    public boolean isAlias(String name) {
        return this.aliasMap.containsKey(name);
    }

    @Override
    public String[] getAliases(String name) {
        List<String> list = new ArrayList<>();
        synchronized (this.aliasMap){

        }
        return StringUtils.toStringArray(list);
    }

    public void resolveAliases(StringValueResolver valueResolver){
        Assert.notNull(valueResolver,"StringValueResolver must not be null");

        //对该对象加锁
        synchronized (this.aliasMap){
            Map<String,String> aliasCopy = new HashMap<>(this.aliasMap);
            aliasCopy.forEach((alias,registeredName)->{
                String resolvedAlias = valueResolver.resolveStringValue(alias);
                String resolvedName = valueResolver.resolveStringValue(registeredName);
                if(resolvedAlias == null || resolvedName == null || resolvedAlias.equals(resolvedName)){
                    this.aliasMap.remove(alias);
                }
                else if(!resolvedAlias.equals(alias)){
                    //新的别名
                    String existingName = this.aliasMap.get(resolvedAlias);
                    if(existingName != null){
                        if(existingName.equals(resolvedName)){
                            this.aliasMap.remove(alias);
                            return;
                        }
                        throw new IllegalStateException("Cannot register resolved alias '" + resolvedAlias + "' (original: '" + alias +
                                        "') for name '" + resolvedName + "': It is already registered for name '" +
                                        registeredName + "'.");
                    }
                    checkForAliasCircle(resolvedName,resolvedAlias);
                    this.aliasMap.remove(alias);
                    this.aliasMap.put(resolvedAlias,resolvedName);
                }else if(!registeredName.equals(resolvedName)){
                    this.aliasMap.put(alias,resolvedName);
                }
            });
        }
    }

    /**
     * 递归的去找别名所对应的所有别名.
     * @param name
     * @param result
     */
    private void retrieveAliases(String name,List<String> result){
        this.aliasMap.forEach((k,v)->{
            if(v.equals(name)){
                result.add(k);
                retrieveAliases(k,result);
            }
        });
    }

    /**
     * 判断该别名是否可以注册
     * @param name
     * @param alias
     */
    protected void checkForAliasCircle(String name,String alias){
        if(hasAlias(alias,name)){
            throw new IllegalStateException("Cannot register alias '" + alias +
                    "' for name '" + name + "': Circular reference - '" +
                    name + "' is a direct or indirect alias for '" + alias + "' already");
        }
    }

    /**
     * 确定原始名称,将给定的别名解析成规范的别名
     * @param name
     * @return
     */
    public String canonicalName(String name){
        String canonicalName = name;
        String resolvedName;
        do{
            resolvedName = this.aliasMap.get(canonicalName);
            if(resolvedName != null){
                canonicalName = resolvedName;
            }
        }while(resolvedName != null);
        return canonicalName;
    }
}
