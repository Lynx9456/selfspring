/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.cycleDependency;

import my.self.cycleDependency.proxy.JdkProxyBeanPostProcessor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一级缓存存放的是完整的实例对象（属性+JDK代理），二级缓存存放的是半成品（属性未完成，JDK代理已完成），三级缓存存放的更半成品（JDK代理未完成）
 *
 * @author 秋涩
 * @version MainTest.java, v 0.1 2025年02月15日 15:09 秋涩
 */
public class MainTest {

    private static Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    //定义一个（一级）缓存，这个缓存中需要保存我们的bean对象，避免多次创建
    private static Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    /**
     * 如果只有一个singletonObjects 这个一级缓存，至少存在两个问题：
     * 1.singletonObjects 里面存放的是classA 和 classB 的不完整对象，如果此时有其他线程进行getBean的调用，会得到不完整的对象。
     * singletonObjects 里面最终会存储两种对象，第一种是没有循环依赖的普通完整对象，第二种是有循环依赖关系的classA 和 classB 的不完整对象
     * 2.singletonObjects 如果仅仅存储我们的完整对象，不存 不完整的对象，那么程序就无法运行，无法从根本上解决循环依赖的问题
     * 因此引入二级缓存，存储不完整的对象
     */
    private static Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

    /**
     * 现在，我们发现如果存在两个或者两个以上的类出现循环依赖问题，并且这些类的一个或者多个存在方法增强AOP，会出现一些问题。
     * 这些问题就是我们之前所说的，如果一个类有AOP，直接使用二级缓存会导致每次拿到的都是最初始的那个instance（无参构造new出来的）。
     * 我们应该期望拿到每次动态代理生成的类，但是现实中，spring中的生命周期顺序是：
     * 实例化 -> 属性填充（进行循环依赖的属性填充）-> 初始化 -> AOP（AOP每次可能生成不同的代理对象，可是由于AOP的位置太靠后了，
     * 没办法干预到属性填充，就导致属性填充过程中，没办法使用到AOP生成的新的代理类，导致依赖注入的错误）
     * 所以，我们需要引入一个三级缓存，这个三级缓存，需要"有实力"将我们的AOP操作，移动到实例化之后，并且在属性填充之前。
     * 所以，我们的三级缓存，需要有延迟加载的功能（钩子，函数式编程，回调）
     */
    private static Map<String, ObjectFactory> singletonFactories = new ConcurrentHashMap<>();

    // 判断当前循环依赖的bean 是否正在创建中
    private static Set<String> singletonCurrentlyInCreation = new HashSet<>();

    public static void loadBeanDefinitions() {
        RootBeanDefinition abd = new RootBeanDefinition(ClassA.class);
        RootBeanDefinition bbd = new RootBeanDefinition(ClassB.class);
        beanDefinitionMap.put("classA", abd);
        beanDefinitionMap.put("classB", bbd);
    }

    public static void main(String[] args) throws Exception {
        loadBeanDefinitions();
        for (String beanName : beanDefinitionMap.keySet()) {
            getBean(beanName);  //getBean方法里有createBean的部分的，缓存的使用肯定是在这个方法中才用。
        }
        ClassA a = (ClassA) getBean("classA");
        a.execute();
    }

    private static Object getBean(String beanName) throws Exception {
        Object singleton = getSingleton(beanName);
        if (singleton != null) {
            return singleton;
        }

        if (!singletonCurrentlyInCreation.contains(beanName)) {
            singletonCurrentlyInCreation.add(beanName);
        }

        //第一步 实例化： 进行bean的实例化
        RootBeanDefinition bd = (RootBeanDefinition) beanDefinitionMap.get(beanName);
        Class<?> clazz = bd.getBeanClass();

        //调用了无参构造方法
        Object instance = clazz.newInstance();
        //earlySingletonObjects.put(beanName, instance);

        //如果有循环依赖并且有AOP代理，必须将classA 的方法增强挪到这里
        //第四步：方法增强（AOP），三级缓存.put() 直接添加（延迟添加）
        if (singletonCurrentlyInCreation.contains(beanName)) {
            singletonFactories.put(beanName,
                    () -> new JdkProxyBeanPostProcessor()
                            .getEarlyBeanReference(instance, beanName));
        }

        //第二步 属性填充：属性填充（在我们源码里也是有相关的方法，只不过此处我们为了演示，简单搞一搞）
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Autowired annotation = field.getAnnotation(Autowired.class);
            if (annotation != null) {
                field.setAccessible(true);
                String name = field.getName();
                Object dep = getBean(name);
                field.set(instance, dep);
            }
        }

        //第三步： 初始化（调用一些init - method）

        //第四步： 方法增强 AOP

        //添加到缓存
        //刚开始spring认为，这个缓存已经足够，因为每一个对象的创建完成以后，都会得到一个完整的对象，可以放到一个缓存中，但是这没有考虑到一些问题
        singletonObjects.put(beanName, instance);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
        return instance;
    }

    private static Object getSingleton(String beanName) {
        //Object singleton = singletonObjects.get(beanName);
        ////一级缓存中没有 && 该instance在创建中
        //if (singleton == null && singletonCurrentlyInCreation.contains(beanName)) {
        //    singleton = earlySingletonObjects.get(beanName);
        //    if (singleton == null) {
        //        ObjectFactory factory = singletonFactories.get(beanName);
        //        if (factory != null) {
        //            singleton = factory.getObject();    // 触发代理生成
        //            //之所以有这句话，就是之前咱们说了半天的，AOP生成的代理对象，需要替换二级缓存中初始生成的那个对象
        //            earlySingletonObjects.put(beanName, singleton);
        //            singletonFactories.remove(beanName);
        //        }
        //    }
        //}

        Object singleton = singletonObjects.get(beanName);
        //一级缓存中没有 && 该instance在创建中
        if (singleton == null && singletonCurrentlyInCreation.contains(beanName)) {
            // 先检查三级缓存
            ObjectFactory factory = singletonFactories.get(beanName);
            if (factory != null) {
                singleton = factory.getObject();
                earlySingletonObjects.put(beanName, singleton); // 升级到二级缓存
                singletonFactories.remove(beanName);
            } else {
                // 最后回退到二级缓存
                singleton = earlySingletonObjects.get(beanName);
            }
        }
        return singleton;
    }
}