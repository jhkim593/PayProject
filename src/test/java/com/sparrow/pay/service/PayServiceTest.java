package com.sparrow.pay.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
@Transactional
class PayServiceTest {

    @Autowired
    PayService payService;

    @Test
    public void dbConnectTest()throws Exception{
        String name="test";
        payService.createCard(name);

    }

}