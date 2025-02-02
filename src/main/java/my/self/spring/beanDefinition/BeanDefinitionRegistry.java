/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.spring.beanDefinition;

/**
 * @author 秋涩
 * @version BeanDefinitionRegistry.java, v 0.1 2025年02月02日 15:43 秋涩
 */
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, AnnotationBeanDefinition beanDefinition);
}