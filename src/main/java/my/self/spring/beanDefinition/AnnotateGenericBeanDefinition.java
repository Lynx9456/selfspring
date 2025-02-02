/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.spring.beanDefinition;

/**
 * @author 秋涩
 * @version AnnotateGenericBeanDefinition.java, v 0.1 2025年02月01日 23:17 秋涩
 */
public class AnnotateGenericBeanDefinition implements AnnotationBeanDefinition{

    private Class clazz;
    private String scope;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}