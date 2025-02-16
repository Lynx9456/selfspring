/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.cycleDependency.proxy;

import my.self.cycleDependency.ClassA;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author 秋涩
 * @version JdkProxyBeanPostProcessor.java, v 0.1 2025年02月15日 23:36 秋涩
 */

@Component
public class JdkProxyBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {

    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        if (bean instanceof ClassA){
            JdkDynamicProxy jdkDynamicProxy = new JdkDynamicProxy(bean);
            return jdkDynamicProxy.getProxy();
        }
        return bean;
    }
}