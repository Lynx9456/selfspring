/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.spring.beanDefinition;

import my.self.spring.annotation.Scope;

/**
 * @author 秋涩
 * @version AnnotateBeanDefinitionReader.java, v 0.1 2025年02月01日 23:01 秋涩
 */
public class AnnotateBeanDefinitionReader {

    private BeanDefinitionRegistry registry;

    // 注册我们的 路径扫描 这个bean到BeanFactory中
    public void register(Class<?> componentClass) {
        registerBean(componentClass);
    }

    private void registerBean(Class<?> componentClass) {
        doRegisterBean(componentClass);
    }

    private void doRegisterBean(Class<?> componentClass) {
        // 把AppConfig 读成一个 BeanDefinition定义
        AnnotateGenericBeanDefinition beanDefinition = new AnnotateGenericBeanDefinition();
        beanDefinition.setClazz(componentClass);
        if (componentClass.isAnnotationPresent(Scope.class)) {
            String scope = componentClass.getAnnotation(Scope.class).value();
            beanDefinition.setScope(scope);
        } else {
            beanDefinition.setScope("singleton");
        }

        // beanDefinition 创建完成后，是不是得给BeanFactory 进行 bean 注册了呀？
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinition,this.registry);
    }
}