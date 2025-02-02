/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.test;

import my.self.spring.applicationContext.AnnotationConfigApplicationContext;
import my.self.test.config.AppConfig;

/**
 * @author 秋涩
 * @version SpringTest.java, v 0.1 2025年02月01日 21:24 秋涩
 */
public class SpringTest {
    public static void main(String[] args) {
        // 创建 applicationContext（注解的形式）
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        //调用getBean

        //多例的，每次获取bean都是新创建的bean，对象地址不一样
        System.out.println(applicationContext.getBean("userService"));
        System.out.println(applicationContext.getBean("userService"));

        //单例的，每次获取bean都是一样的地址值
        System.out.println(applicationContext.getBean("userService1"));
        System.out.println(applicationContext.getBean("userService1"));
    }
}