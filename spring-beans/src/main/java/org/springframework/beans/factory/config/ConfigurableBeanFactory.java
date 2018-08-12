package org.springframework.beans.factory.config;

import com.sun.istack.internal.Nullable;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.SingletonBeanRegistry;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringValueResolver;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory,SingletonBeanRegistry {


    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 为该bean factory设置父beanFactory
     */
    void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;

    /**
     * 设置类加载器
     * @param beanClassLoader
     */
    void setBeanClassLoader(@Nullable ClassLoader beanClassLoader);

    /**
     * 获取该beanFactory的类加载器
     * @return
     */
    @Nullable
    ClassLoader getBeanClassLoader();

    /**
     *
     * @param tempClassLoader
     */
    void setTempClassLoader(@Nullable ClassLoader tempClassLoader);

    @Nullable
    ClassLoader getTempClassLoader();

    /**
     * 是否需要缓存bean metadata,比如bean difinition 和 解析好的classes.默认开启缓存
     */
    void setCacheBeanMetadata(boolean cacheBeanMetadata);

    /**
     * 是否开启了缓存
     * @return
     */
    boolean isCacheBeanMetadata();

    /**
     * 定义用于解析bean definition的表达式解析器
     */
    void setBeanExpressionResolver(@Nullable BeanExpressionResolver resolver);

    /**
     * 获取解析bean definition的表达式解析器
     */
    @Nullable
    BeanExpressionResolver getBeanExpressionResolver();

    /**
     * 类型转化器
     */
    void setConversionService(@Nullable ConversionService conversionService);

    @Nullable
    ConversionService getConversionService();

    /**
     * 属性编辑器
     */
    void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);

    /**
     * BeanFactory用来转换bean属性值或者参数值的自定义转换器
     */
    void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass);

    /**
     * 初始化了自定义编辑propertyeditorregistry
     * 已经注册了这个BeanFactory。
     */
    void copyRegisteredEditorsTo(PropertyEditorRegistry registry);

    /**
     * 类型转换器
     * 设置这个Bean工厂应Bean属性转换，构造函数参数值等使用自定义类型转换器
     * 这将覆盖默认的PropertyEditor机制，因此使任何自定义编辑器或自定义编辑器注册表都无关紧要。
     */
    void setTypeConverter(TypeConverter typeConverter);
    TypeConverter getTypeConverter();

    /**
     * 为嵌入值（例如注释属性）添加String解析器。
     * 为注解属性嵌入字符串解析器
     */
    void addEmbeddedValueResolver(StringValueResolver valueResolver);
    boolean hasEmbeddedValueResolver();

    /**
     * 解析给定的嵌入值，例如 注释属性。
     */
    String resolveEmbeddedValue(String value);

    /**
     * 添加一个新的BeanPostProcessor，它将应用于此工厂创建的bean。 在工厂配置期间调用。
     * 注意：此处提交的后处理器将按注册顺序应用; 通过实现Ordered接口表达的任何排序语义都将被忽略。
     * 请注意，自动检测的后处理器（例如，作为ApplicationContext中的bean）将始终在以编程方式注册后应用。
     */
    void addBeanPostProcessorCount(BeanPostProcessor beanPostProcessor);

    /**
     * return the current number of registered beanpostProessors,if any
     * 返回已经注册了的beanPostProcessors,如果有的话
     */
    int getBeanPostProcessorCount();

    /**
     * 注册给定的scope,返回给的scopre的实现
     * @param scopreName
     * @param scope
     */
    void registerScope(String scopreName,Scope scope);

    String[] getRegisteredScopeNames();

    /**
     * 返回给定的scope name的实现,如果有
     * @param scopeName
     * @return
     */
    @Nullable
    Scope getRegisteredScope(String scopeName);

    /**
     * 提供与此工厂相关的安全访问控制上下文
     */
    AccessControlContext getAccessControlContext();

    /**
     * 复制给定其他工厂的所有相关配置。
     * 应包括所有标准配置设置以及BeanPostProcessors，Scopes和工厂特定的内部设置。
     * 不应包含实际bean定义的任何元数据，例如BeanDefinition对象和bean名称别名。
     */
    void copyConfigurationFrom(ConfigurableBeanFactory otherFactory);

    /**
     * 给一个beanName,创建一个别名.我们通常使用此方法来支持XML ID中非法的名称（用于bean名称）。
     * 通常在工厂配置期间调用，但也可用于别名的运行时注册。 因此，工厂实现应同步别名访问。
     * @param beanName
     * @param alias
     * @throws BeanDefinitionStoreException
     */
    void registerAlias(String beanName,String alias) throws BeanDefinitionStoreException;

    /**
     * 解析在此工厂中注册的所有别名目标名称和别名，将给定的StringValueResolver应用于它们。
     * 例如，值解析器可以解析目标bean名称中的占位符，甚至可以解析别名中的占位符。
     */
    void resolveAliases(StringValueResolver valueResolver);


    /**
     * 返回给定bean名称的合并BeanDefinition，
     * 如果需要，将子bean定义与其父bean合并。
     * 也考虑父工厂中的bean定义。
     * @param beanName
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

    /**
     * 确定给定名称的bean是否为FactoryBean。
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException;

    /**
     * 显式控制指定bean的当前创建状态。 仅限容器内部使用。
     * @param beanName
     * @param inCreation
     */
    void setCurrentlyInCreation(String beanName, boolean inCreation);

    /**
     * 为给定的bean注册依赖bean
     * 在销毁给定bean之前销毁它
     * @param beanName
     */
    void registerDependentBean(String beanName);

    /**
     * 获取给定bean的所有依赖bean的名字
     * @param beanName
     * @return
     */
    String[] getDependentBeans(String beanName);

    /**
     * 返回指定bean所依赖的所有bean的名称（如果有）
     * @param beanName
     * @return
     */
    String[] getDependenciesForBean(String beanName);
    /**
     * 根据bean的定义，销毁给定的bean实例（通常是从这个工厂获得的原型实例）。
     * 应该捕获并记录在销毁期间出现的任何异常，而不是传播给此方法的调用方。
     * @param beanName
     * @param beanInstance
     */
    void destroyBean(String beanName,Object beanInstance);

    /**
     * 销毁当前目标作用域中指定的作用域bean（如果有）。
     * 应该捕获并记录在销毁期间出现的任何异常，而不是传播给此方法的调用方。
     * @param beanName
     */
    void destroyScopedBean(String beanName);

    /**
     * 销毁此工厂中的所有单例bean，包括已注册为一次性的内部bean。 在关闭工厂时被调用。
     * 应该捕获并记录在销毁期间出现的任何异常，而不是传播给此方法的调用方。
     */
    void destroySingletons();
}
