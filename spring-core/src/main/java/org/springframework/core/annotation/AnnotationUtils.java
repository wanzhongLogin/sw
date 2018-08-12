package org.springframework.core.annotation;


import com.sun.istack.internal.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringUtils;

import javax.print.attribute.standard.MediaSize;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AnnotationUtils {


    public static final String VALUE = "value";

    //缓存map
    private static final Map<AnnotationCacheKey,Annotation> findAnnotationCache = new ConcurrentReferenceHashMap<>(256);

    private static final Map<AnnotationCacheKey,Boolean> metaPresentCache = new ConcurrentReferenceHashMap<>(256);

    private static final Map<Class<?>,Set<Method>> annotatedBaseTypeCache = new ConcurrentReferenceHashMap<>(256);


    //just here for old tool versions trying to reflectively clear the cache
    @SuppressWarnings("unused")
    @Deprecated
    private static final Map<Class<?>,?> annotatedInterfaceCache = annotatedBaseTypeCache;

    private static final Map<Class<? extends Annotation>,Boolean> synthesizableCacche = new ConcurrentReferenceHashMap <>(256);

    private static final Map<Class<? extends Annotation>,Map<String,List<String>>> attributeAliasesCache
            = new ConcurrentReferenceHashMap<>(256);

    private static final Map<Class<? extends Annotation>, List<Method>> attributeMethodsCache =
            new ConcurrentReferenceHashMap<>(256);

    private static final Map<Method, AnnotationUtils.AliasDescriptor> aliasDescriptorCache =
            new ConcurrentReferenceHashMap<>(256);



    public static boolean isAnnotationMetaPresent(Class<? extends Annotation> annotationType,@Nullable Class<? extends Annotation> metaAnnotationType){
        Assert.notNull(annotationType,"annotation type must not be null");
        if(metaAnnotationType == null){
            return false;
        }
        AnnotationCacheKey cacheKey = new AnnotationCacheKey(annotationType,metaAnnotationType);
        Boolean metaPresent = metaPresentCache.get(cacheKey);

        if(metaPresent != null){
            return metaPresent;
        }
        metaPresent = Boolean.FALSE;

        return metaPresent;
    }

    /**
     * 注解缓存Key
     * 内部类
     */
    private static final class AnnotationCacheKey implements Comparable<AnnotationCacheKey>{
        private final AnnotatedElement element;

        private final Class<? extends Annotation> annotationType;

        public AnnotationCacheKey(AnnotatedElement element,Class<? extends Annotation> annotationType) {
            this.element = element;
            this.annotationType = annotationType;
        }

        @Override
        public boolean equals(Object other) {
            if(this == other){
                return true;
            }
            if(!(other instanceof AnnotationCacheKey)){
                return false;
            }
            AnnotationCacheKey otherKey = (AnnotationCacheKey) other;
            return (this.element.equals(otherKey.element) && this.annotationType.equals(otherKey.annotationType));
        }

        @Override
        public int hashCode() {
            return (this.element.hashCode() * 29 + this.annotationType.hashCode());
        }

        @Override
        public String toString() {
            return "@" + this.annotationType + " on " + this.element;
        }

        @Override
        public int compareTo(AnnotationCacheKey other) {
            int result = this.element.toString().compareTo(other.element.toString());
            if (result == 0) {
                result = this.annotationType.getName().compareTo(other.annotationType.getName());
            }
            return result;
        }
    }

    private static final class AliasDescriptor{

        private final Method sourceAttribute;
        private final Class<? extends Annotation> sourceAnnotationType;
        private final String sourceAttributeName;
//        private final Method aliasedAttribute;
        private final Class<? extends Annotation> aliasedAnnotationType;
//        private final String aliasedAttributeName;
//        private final boolean isAliasPair;


        public static AliasDescriptor from(Method attribute){
            AliasDescriptor descriptor = aliasDescriptorCache.get(attribute);
            if(descriptor != null){
                return descriptor;
            }

            AliasFor aliasFor = attribute.getAnnotation(AliasFor.class);

            if(aliasFor == null){
                return null;
            }

            descriptor = new AliasDescriptor(attribute,aliasFor);



            return null;
        }


        private  AliasDescriptor(Method sourceAttribute,AliasFor aliasFor){

            Class<?> declaringClass = sourceAttribute.getDeclaringClass();

            this.sourceAttribute = sourceAttribute;
            this.sourceAnnotationType = (Class <? extends Annotation>) declaringClass;
            this.sourceAttributeName =  sourceAttribute.getName();

            this.aliasedAnnotationType = (Annotation.class == aliasFor.annotation() ? this.sourceAnnotationType : aliasFor.annotation());

        }

        private void validte(){
//            if(!this.isAliasPair && !isAnnotation){
//
//            }
        }

        private String getAliasedAttributeName(AliasFor aliasFor,Method attribute){

            String attributeName = aliasFor.attribute();
            String value = aliasFor.value();

            boolean attributeDeclared = StringUtils.hasText(attributeName);
            boolean valueDeclared = StringUtils.hasText(value);

            if(attributeDeclared && valueDeclared){
                String msg = String.format("In @AliasFor declared on attribute '%s' in annotation [%s], attribute 'attribute' " +
                                "and its alias 'value' are present with values of [%s] and [%s], but only one is permitted.",
                        attribute.getName(), attribute.getDeclaringClass().getName(), attributeName, value);
                throw new AnnotationConfigurationException(msg);
            }
            attributeName = (attributeDeclared ? attributeName : value);
            return (StringUtils.hasText(attributeName) ? attributeName.trim() : attribute.getName());
        }


    }


}
