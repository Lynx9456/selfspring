/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.spring.applicationContext;

import my.self.spring.beanDefinition.AnnotateBeanDefinitionReader;
import my.self.spring.beanDefinition.BeanDefinitionRegistry;

/**
 * @author 秋涩
 * @version AnnotationConfigApplicationContext.java, v 0.1 2025年02月01日 23:00 秋涩
 */
public class AnnotationConfigApplicationContext extends GenericApplicationContext implements BeanDefinitionRegistry {

    private AnnotateBeanDefinitionReader reader;

    // 如果有人调用这个无参构造方法，必须先调用父类的无参构造方法，父类初始化 defaultListableBeanFactory
    public AnnotationConfigApplicationContext() {
        this.reader = new AnnotateBeanDefinitionReader(this);
    }

    public AnnotationConfigApplicationContext(Class<?> componentClass) {
        //1. 读componentClass 也就是我们的 扫描路径 所在的类 AppConfig，定义一个阅读器
        // 专门读取 AnnotateBeanDefinitionReader
        this();

        //2.先把这个类AppConfig 注册到bean工厂里(BeanDefinition + RegisterBeanDefinition + FactoryBean)
        register(componentClass);

        //3. 扫描这个路径，然后提取出这个路径下所有的bean，然后注册到bean工厂（单例bean的初始化）

    }

    private void register(Class<?> componentClass) {
        this.reader.register(componentClass);
    }
}