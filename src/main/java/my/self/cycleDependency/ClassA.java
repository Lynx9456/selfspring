/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.cycleDependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 秋涩
 * @version ClassA.java, v 0.1 2025年02月15日 15:08 秋涩
 */

@Component
public class ClassA implements IClassA{

    @Autowired
    private ClassB classB;

    public ClassA() {
        System.out.println("ClassA init success !!!");
    }

    public ClassA(ClassB classB) {
        this.classB = classB;
    }

    public ClassB getClassB() {
        return classB;
    }

    public void setClassB(ClassB classB) {
        this.classB = classB;
    }

    @Override
    public void execute() {
        System.out.println("i am Class A");
    }
}