package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.*;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringValueResolver;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;

/**
 * 综合FactoryBeanRegistrySupport和ConfigurableBeanFactory的功能
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {
    @Override
    public void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException {

    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {

    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return null;
    }

    @Override
    public void setTempClassLoader(ClassLoader tempClassLoader) {

    }

    @Override
    public ClassLoader getTempClassLoader() {
        return null;
    }

    @Override
    public void setCacheBeanMetadata(boolean cacheBeanMetadata) {

    }

    @Override
    public boolean isCacheBeanMetadata() {
        return false;
    }

    @Override
    public void setBeanExpressionResolver(BeanExpressionResolver resolver) {

    }

    @Override
    public BeanExpressionResolver getBeanExpressionResolver() {
        return null;
    }

    @Override
    public void setConversionService(ConversionService conversionService) {

    }

    @Override
    public ConversionService getConversionService() {
        return null;
    }

    @Override
    public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {

    }

    @Override
    public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass) {

    }

    @Override
    public void copyRegisteredEditorsTo(PropertyEditorRegistry registry) {

    }

    @Override
    public void setTypeConverter(TypeConverter typeConverter) {

    }

    @Override
    public TypeConverter getTypeConverter() {
        return null;
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {

    }

    @Override
    public boolean hasEmbeddedValueResolver() {
        return false;
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        return null;
    }

    @Override
    public void addBeanPostProcessorCount(BeanPostProcessor beanPostProcessor) {

    }

    @Override
    public int getBeanPostProcessorCount() {
        return 0;
    }

    @Override
    public void registerScope(String scopreName, Scope scope) {

    }

    @Override
    public String[] getRegisteredScopeNames() {
        return new String[0];
    }

    @Override
    public Scope getRegisteredScope(String scopeName) {
        return null;
    }

    @Override
    public AccessControlContext getAccessControlContext() {
        return null;
    }

    @Override
    public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {

    }

    @Override
    public BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return null;
    }

    @Override
    public boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public void registerDependentBean(String beanName) {

    }

    @Override
    public void destroyBean(String beanName, Object beanInstance) {

    }

    @Override
    public void destroyScopedBean(String beanName) {

    }

    @Override
    public BeanFactory getParentBeanFactory() {
        return null;
    }

    @Override
    public boolean containsLocation(String name) {
        return false;
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return null;
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return null;
    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return null;
    }
}
