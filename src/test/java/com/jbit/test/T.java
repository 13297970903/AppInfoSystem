package com.jbit.test;

import com.jbit.pojo.DevUser;
import com.jbit.service.DevUserService;
import org.apache.ibatis.javassist.ClassPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class T {
    
    @Resource
    private DevUserService devUserService;

    @Test
    public void name() {
        DevUser devUser = devUserService.queryLogin("test001", "123456");
        System.out.println("devUser = " + devUser);
    }
}
