/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.spring.applicationContext;

import my.self.spring.beanDefinition.AnnotationBeanDefinition;
import my.self.spring.beanDefinition.BeanDefinitionRegistry;
import my.self.spring.beanFactory.BeanFactory;

/**
 * @author 秋涩
 * @version GenericApplicationContext.java, v 0.1 2025年02月02日 17:08 秋涩
 */
public class GenericApplicationContext implements BeanDefinitionRegistry {

    private DefaultListableBeanFactory factory;

    public GenericApplicationContext() {
        this.factory = new DefaultListableBeanFactory();
    }

    @Override
    public void registerBeanDefinition(String beanName, AnnotationBeanDefinition beanDefinition) {
        this.factory.registerBeanDefinition(beanName, beanDefinition);
    }
}