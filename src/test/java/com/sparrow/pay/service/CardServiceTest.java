//package com.sparrow.pay.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//@Transactional
//class CardServiceTest {
//
//    @Autowired
//    CardService cardService;
//
//    @Test
//    @Rollback(value = false)
//    public void dbConnectTest()throws Exception{
//        String name="test";
//        cardService.createCard(name);
//
//    }
//
//}