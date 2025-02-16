/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.cycleDependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 秋涩
 * @version ClassB.java, v 0.1 2025年02月15日 15:08 秋涩
 */

@Component
public class ClassB {

    @Autowired
    private ClassA classA;

    public ClassB() {
        System.out.println("ClassB init success !!!");
    }

    public ClassB(ClassA classA) {
        this.classA = classA;
    }

    public ClassA getClassA() {
        return classA;
    }

    public void setClassA(ClassA classA) {
        this.classA = classA;
    }
}