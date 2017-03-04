package com.pay.memcached;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.danga.MemCached.MemCachedClient;
import com.pay.model.ReturnMessage;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "../../../applicationContext.xml"})
public class MemcachedSpringTest {

    private MemCachedClient cachedClient;
    @Before
    public void init() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        cachedClient = (MemCachedClient)context.getBean("memcachedClient");
    }
    @Test
    public void testMemcachedSpring() {
        ReturnMessage returnMessage = new ReturnMessage(1, "derek");
        
        cachedClient.set("user", returnMessage);

        ReturnMessage cachedBean = (ReturnMessage)returnMessage;

        System.out.println(returnMessage.equals(cachedBean ));

    }
  }