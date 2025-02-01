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
    }
}