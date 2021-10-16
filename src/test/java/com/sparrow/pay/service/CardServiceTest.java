package com.sparrow.pay.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.transaction.annotation.Transactional;



@SpringBootTest
@Transactional
class CardServiceTest {

    @Autowired
    CardService cardService;

    @Test
    public void dbConnectTest()throws Exception{
        String name="test";
        cardService.createCard(name);

    }

}