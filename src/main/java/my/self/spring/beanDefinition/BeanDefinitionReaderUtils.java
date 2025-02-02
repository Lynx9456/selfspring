/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.spring.beanDefinition;

/**
 * @author 秋涩
 * @version BeanDefinitionReaderUtils.java, v 0.1 2025年02月02日 15:40 秋涩
 */
public class BeanDefinitionReaderUtils {
    public static void registerBeanDefinition(AnnotationBeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        String beanName = ((AnnotateGenericBeanDefinition) beanDefinition).getClazz().getSimpleName();
        registry.registerBeanDefinition(beanName, beanDefinition);
    }
}