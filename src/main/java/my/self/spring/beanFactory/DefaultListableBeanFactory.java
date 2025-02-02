/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.spring.beanFactory;

import my.self.spring.annotation.ComponentScan;
import my.self.spring.annotation.Scope;
import my.self.spring.annotation.Service;
import my.self.spring.beanDefinition.AnnotateGenericBeanDefinition;
import my.self.spring.beanDefinition.AnnotationBeanDefinition;
import my.self.spring.beanDefinition.BeanDefinitionRegistry;
import my.self.spring.beanFactory.BeanFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 秋涩
 * @version DefaultListableBeanFactory.java, v 0.1 2025年02月02日 17:05 秋涩
 */
public class DefaultListableBeanFactory implements BeanDefinitionRegistry, BeanFactory {

    private final Map<String, AnnotationBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    private List<String> beanDefinitionNames = new ArrayList<>();

    @Override
    public void registerBeanDefinition(String beanName, AnnotationBeanDefinition beanDefinition) {
        //源码里有一些其他逻辑
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    public void doScan() {
        System.out.println("---");
        for (String beanName : beanDefinitionMap.keySet()) {
            AnnotateGenericBeanDefinition bd = (AnnotateGenericBeanDefinition) beanDefinitionMap.get(beanName);
            if (bd.getClazz().isAnnotationPresent(ComponentScan.class)) {
                ComponentScan componentScan = (ComponentScan) bd.getClazz().getAnnotation(ComponentScan.class);
                String basePackage = componentScan.value();
                URL resource = this.getClass().getClassLoader().getResource(basePackage.replace(".", "/"));
                File file = new File(resource.getFile());
                if (file.isDirectory()) {
                    for (File f : file.listFiles()) {
                        try {
                            Class clazz = this.getClass()
                                    .getClassLoader()
                                    .loadClass(basePackage.concat(".").concat(f.getName().split("\\.")[0]));
                            if (clazz.isAnnotationPresent(Service.class)) {
                                String name = ((Service) clazz.getAnnotation(Service.class)).value();
                                AnnotateGenericBeanDefinition abd = new AnnotateGenericBeanDefinition();
                                abd.setClazz(clazz);
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    abd.setScope(((Scope) clazz.getAnnotation(Scope.class)).value());
                                } else {
                                    abd.setScope("singleton");
                                }
                                beanDefinitionMap.put(name, abd);
                                //需要有一个地方，记录我们真正定义的bean
                                beanDefinitionNames.add(name);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        System.out.println();
    }

    // 只有我们的bean都注册上以后，才能有 getBean
    @Override
    public Object getBean(String beanName) {
        return null;
    }
}