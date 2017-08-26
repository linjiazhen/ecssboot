package com.wintoo.controller;

import org.springframework.beans.factory.InitializingBean;

/**
 * Created by Jason on 16/1/22.
 */
public class Test implements InitializingBean {
    public void afterPropertiesSet() throws Exception {
        System.out.println("******************************");
        System.out.println("afterPropertiesSet is called");
        System.out.println("******************************");
    }
}
