/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.spring.applicationContext;

import my.self.spring.beanDefinition.AnnotationBeanDefinition;
import my.self.spring.beanDefinition.BeanDefinitionRegistry;
import my.self.spring.beanFactory.DefaultListableBeanFactory;

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

    protected void refresh() {
        // 获取bean工厂
        DefaultListableBeanFactory beanFactory = obtainBeanFactory();
        // 把AppConfig路径下的所有bean进行扫描，注册到bean工厂beanDefinitionMap(UserService 和 UserService1)
        invokeBeanFactoryPostProcessors(beanFactory);
        // 初始化BeanDefinition所代表的单例bean，放到单例bean的容器(缓存)里
        finishBeanFactoryInitialization(beanFactory);

    }

    private void invokeBeanFactoryPostProcessors(DefaultListableBeanFactory beanFactory) {
        //这部分，我们进行了一个简化的类，实际源码中doScan方法并没有在beanFactory里
        this.factory.doScan();
    }

    private void finishBeanFactoryInitialization(DefaultListableBeanFactory beanFactory) {
        //todo
    }

    private DefaultListableBeanFactory obtainBeanFactory() {
        return this.factory;
    }
}