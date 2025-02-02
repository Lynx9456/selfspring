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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
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

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

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
        return doGetBean(beanName);
    }

    private Object doGetBean(String beanName) {
        Object bean = singletonObjects.get(beanName);
        if (bean != null) {
            return bean;
        }
        //当bean不存在时，需要根据beanDefinition来创建bean
        AnnotateGenericBeanDefinition bd = (AnnotateGenericBeanDefinition) beanDefinitionMap.get(beanName);
        Object createBean = createBean(beanName, bd);
        //当scope=singleton时，创建对象并放入单例池，通过享元模式进行读取
        //当scope=prototype时，每次调用都会创建新对象
        if (bd.getScope().equals("singleton")) {
            //createBean方法其实是完成了beanDefinition转变成真正的 实体对象的地方
            singletonObjects.put(beanName, createBean);
        }
        return createBean;
    }

    private Object createBean(String beanName, AnnotateGenericBeanDefinition bd) {
        try {
            return bd.getClazz().getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void preInstanceInitiateSingleton() {
        //初始化我们定义的bean，我们就需要找到所有的 我们自定义的beanName

        //为什么不直接使用我们的beanDefinitionNames呢？new了一下，是不是多此一举？
        //因为beanDefinitionNames处于一个并发环境下，因为我们上面还有beanDefinitionNames.add的方法逻辑
        //我们的有关beanDefinitionNames.add元素的方法，就会导致for循环失败(modCount，当add元素后modCount++)
        //所以我们此处的代码，就是备份了一个新的List<String> beanNames对象，防止beanDefinitionNames在并发环境下的add操作
        List<String> beanNames = new ArrayList<>(beanDefinitionNames);
        for (String beanName : beanNames) {
            //beanNames里的东西，都是扫描出来的
            //如果扫描之后，有新的 通过动态创建的 标有单例bean的Class加载到JVM，这部分就会被遗漏
            AnnotateGenericBeanDefinition bd = (AnnotateGenericBeanDefinition) beanDefinitionMap.get(beanName);
            if (bd.getScope().equals("singleton")) {
                //创建单例对象，然后把这个单例对象保存到我们的 单例池(内存缓存)里面
                //getBean方法里面就包含了 创建对象，然后放到singletonObjects里

                //为了确保我们在getBean调用的时候，能够不遗漏应该初始化的单例bean，所以我们把这部分逻辑放到getBean里
                getBean(beanName);
            }
        }

    }
}