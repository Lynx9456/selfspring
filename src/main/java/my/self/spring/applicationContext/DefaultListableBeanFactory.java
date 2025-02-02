/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.spring.applicationContext;

import my.self.spring.beanDefinition.AnnotateGenericBeanDefinition;
import my.self.spring.beanDefinition.AnnotationBeanDefinition;
import my.self.spring.beanDefinition.BeanDefinitionRegistry;
import my.self.spring.beanFactory.BeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 秋涩
 * @version DefaultListableBeanFactory.java, v 0.1 2025年02月02日 17:05 秋涩
 */
public class DefaultListableBeanFactory implements BeanDefinitionRegistry, BeanFactory {

    private final Map<String, AnnotationBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    @Override
    public void registerBeanDefinition(String beanName, AnnotationBeanDefinition beanDefinition) {
        //源码里有一些其他逻辑
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    // 只有我们的bean都注册上以后，才能有 getBean
    @Override
    public Object getBean(String beanName) {
        return null;
    }
}