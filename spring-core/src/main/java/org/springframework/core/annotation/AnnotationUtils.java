package org.springframework.core.annotation;


import org.springframework.util.ConcurrentReferenceHashMap;

import javax.print.attribute.standard.MediaSize;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

public abstract class AnnotationUtils {


    public static final String VALUE = "value";

    //缓存map
    private static final Map<AnnotationCacheKey,Annotation> findAnnotationCache = new ConcurrentReferenceHashMap<>(256);

    private static final Map<AnnotationCacheKey,Boolean> metaPresentCache = new ConcurrentReferenceHashMap<>(256);

    private static final Map<Class<?>,Set<Method>> annotatedBaseTypeCache = new ConcurrentReferenceHashMap<>(256);


    /**
     * 注解缓存Key
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
}
