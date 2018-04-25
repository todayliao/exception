package com.exception.qms.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/23
 * @time 下午5:54
 * @discription
 **/
public class BeanTests {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-test.xml");
        context.start();
        People bean = context.getBean(People.class);
        System.out.println(bean.toString());
    }
}
