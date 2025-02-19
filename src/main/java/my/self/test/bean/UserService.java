/*
 * Ant Group
 * Copyright (c) 2004-2025 All Rights Reserved.
 */
package my.self.test.bean;

import my.self.spring.annotation.Scope;
import my.self.spring.annotation.Service;

/**
 * @author 秋涩
 * @version UserService.java, v 0.1 2025年02月01日 21:24 秋涩
 */

@Service("userService")
@Scope("prototype")
public class UserService {
}