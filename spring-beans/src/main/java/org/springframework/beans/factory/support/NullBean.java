package org.springframework.beans.factory.support;

public final class NullBean {
    public NullBean(){
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj || obj == null);
    }

    @Override
    public int hashCode() {
        return NullBean.class.hashCode();
    }

    @Override
    public String toString() {
        return "null";
    }
}
