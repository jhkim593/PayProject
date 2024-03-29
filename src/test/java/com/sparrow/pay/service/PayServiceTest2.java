package com.sparrow.pay.service;

import com.sparrow.pay.dto.CancelPayRequestDto;
import com.sparrow.pay.dto.CancelPayResponseDto;
import com.sparrow.pay.dto.PayRequestDto;
import com.sparrow.pay.dto.PayResponseDto;
import com.sparrow.pay.entity.Pay;
import com.sparrow.pay.exception.ExceedCancelPayException;
import com.sparrow.pay.exception.ExceedVatException;
import com.sparrow.pay.exception.VatExceedPriceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class PayServiceTest2 {

    @Autowired
    PayService payService;


    @Test
    public void case1()throws Exception {

        //1.결제
        PayRequestDto payRequestDto = new PayRequestDto();
        payRequestDto.setCardNum("1234567890123456");
        payRequestDto.setExpirationDate("1125");
        payRequestDto.setCvc("777");
        payRequestDto.setInstallmentMonth(0);
        payRequestDto.setPrice(11000L);
        payRequestDto.setVat(1000L);
        PayResponseDto payResponseDto = payService.createPay(payRequestDto);

        //2.부분취소 성공
        CancelPayRequestDto cancelPayRequestDto1 = new CancelPayRequestDto();
        cancelPayRequestDto1.setPayId(payResponseDto.getPayId());
        cancelPayRequestDto1.setCancelPrice(1100L);
        cancelPayRequestDto1.setVat(100L);
        payService.createCancelPay(cancelPayRequestDto1);

        //3.부분취소 성공
        CancelPayRequestDto cancelPayRequestDto2 = new CancelPayRequestDto();
        cancelPayRequestDto2.setPayId(payResponseDto.getPayId());
        cancelPayRequestDto2.setCancelPrice(3300L);
        cancelPayRequestDto2.setVat(null);
        payService.createCancelPay(cancelPayRequestDto2);

        //4.부분취소 실패
        CancelPayRequestDto cancelPayRequestDto3=new CancelPayRequestDto();
        cancelPayRequestDto3.setPayId(payResponseDto.getPayId());
        cancelPayRequestDto3.setCancelPrice(7000L);
        cancelPayRequestDto3.setVat(null);
        assertThatThrownBy(()->payService.createCancelPay(cancelPayRequestDto3)).isInstanceOf(ExceedCancelPayException.class);

        //5.부분취소 실패
        CancelPayRequestDto cancelPayRequestDto4=new CancelPayRequestDto();
        cancelPayRequestDto4.setPayId(payResponseDto.getPayId());
        cancelPayRequestDto4.setCancelPrice(6600L);
        cancelPayRequestDto4.setVat(700L);
        assertThatThrownBy(()->payService.createCancelPay(cancelPayRequestDto4)).isInstanceOf(ExceedVatException.class);

        //6.부분취소 성공
        CancelPayRequestDto cancelPayRequestDto5=new CancelPayRequestDto();
        cancelPayRequestDto5.setPayId(payResponseDto.getPayId());
        cancelPayRequestDto5.setCancelPrice(6600L);
        cancelPayRequestDto5.setVat(600L);
        payService.createCancelPay(cancelPayRequestDto5);

        //7.부분취소 실패
        CancelPayRequestDto cancelPayRequestDto6=new CancelPayRequestDto();
        cancelPayRequestDto6.setPayId(payResponseDto.getPayId());
        cancelPayRequestDto6.setCancelPrice(100L);
        cancelPayRequestDto6.setVat(null);
        assertThatThrownBy(()->payService.createCancelPay(cancelPayRequestDto4)).isInstanceOf(ExceedCancelPayException.class);

    }
    @Test
    public void case2()throws Exception{

        //1.결제
        PayRequestDto payRequestDto=new PayRequestDto();
        payRequestDto.setCardNum("1234567890123456");
        payRequestDto.setExpirationDate("1125");
        payRequestDto.setCvc("777");
        payRequestDto.setInstallmentMonth(0);
        payRequestDto.setPrice(20000L);
        payRequestDto.setVat(909L);
        PayResponseDto payResponseDto = payService.createPay(payRequestDto);

        //2.부분취소 성공
        CancelPayRequestDto cancelPayRequestDto1=new CancelPayRequestDto();
        cancelPayRequestDto1.setPayId(payResponseDto.getPayId());
        cancelPayRequestDto1.setCancelPrice(10000L);
        cancelPayRequestDto1.setVat(0L);
        payService.createCancelPay(cancelPayRequestDto1);

        //3.부분취소 실패
        CancelPayRequestDto cancelPayRequestDto2=new CancelPayRequestDto();
        cancelPayRequestDto2.setPayId(payResponseDto.getPayId());
        cancelPayRequestDto2.setCancelPrice(10000L);
        cancelPayRequestDto2.setVat(0L);
        assertThatThrownBy(()->payService.createCancelPay(cancelPayRequestDto2)).isInstanceOf(VatExceedPriceException.class);

        //4.부분취소 성공
        CancelPayRequestDto cancelPayRequestDto3=new CancelPayRequestDto();
        cancelPayRequestDto3.setPayId(payResponseDto.getPayId());
        cancelPayRequestDto3.setCancelPrice(10000L);
        cancelPayRequestDto3.setVat(909L);
        payService.createCancelPay(cancelPayRequestDto3);

    }
    @Test
    public void case3()throws Exception{

        //1.결제
        PayRequestDto payRequestDto=new PayRequestDto();
        payRequestDto.setCardNum("1234567890123456");
        payRequestDto.setExpirationDate("1125");
        payRequestDto.setCvc("777");
        payRequestDto.setInstallmentMonth(0);
        payRequestDto.setPrice(20000L);
        payRequestDto.setVat(null);
        PayResponseDto payResponseDto = payService.createPay(payRequestDto);

        //2.부분취소 성공
        CancelPayRequestDto cancelPayRequestDto1=new CancelPayRequestDto();
        cancelPayRequestDto1.setPayId(payResponseDto.getPayId());
        cancelPayRequestDto1.setCancelPrice(10000L);
        cancelPayRequestDto1.setVat(1000L);
        payService.createCancelPay(cancelPayRequestDto1);

        //3.부분취소 실패
        CancelPayRequestDto cancelPayRequestDto2=new CancelPayRequestDto();
        cancelPayRequestDto2.setPayId(payResponseDto.getPayId());
        cancelPayRequestDto2.setCancelPrice(10000L);
        cancelPayRequestDto2.setVat(909L);
        assertThatThrownBy(()->payService.createCancelPay(cancelPayRequestDto2)).isInstanceOf(ExceedVatException.class);

        //4.부분취소 성공
        CancelPayRequestDto cancelPayRequestDto3=new CancelPayRequestDto();
        cancelPayRequestDto3.setPayId(payResponseDto.getPayId());
        cancelPayRequestDto3.setCancelPrice(10000L);
        cancelPayRequestDto3.setVat(null);
        CancelPayResponseDto cancelPay = payService.createCancelPay(cancelPayRequestDto3);

        assertThat(cancelPay.getOriPrice()).isEqualTo(0L);
        assertThat(cancelPay.getOriVat()).isEqualTo(0L);
    }
    /**
     * 같은 결제에 대해 동시성 Test**/

    private ExecutorService ex = Executors.newFixedThreadPool(100);
    private CountDownLatch latch=new CountDownLatch(100);


    @Test
    public void lockTest()throws Exception{

        PayRequestDto payRequestDto=new PayRequestDto();
        payRequestDto.setCardNum("1234567890123456");
        payRequestDto.setExpirationDate("1125");
        payRequestDto.setCvc("777");
        payRequestDto.setInstallmentMonth(0);
        payRequestDto.setPrice(20000L);
        payRequestDto.setVat(5000L);
        PayResponseDto payResponseDto = payService.createPay(payRequestDto);
        List<CancelPayResponseDto>list=new ArrayList<>();

        for(int i=0; i<100;i++){
            ex.execute(()->{
                try {
                    CancelPayRequestDto cancelPayRequestDto=new CancelPayRequestDto();
                    cancelPayRequestDto.setPayId(payResponseDto.getPayId());
                    cancelPayRequestDto.setCancelPrice(100L);
                    cancelPayRequestDto.setVat(10L);
                    CancelPayResponseDto cancelPay = payService.createCancelPay(cancelPayRequestDto);
                    list.add(cancelPay);
                } catch (Exception e) {

                }
                latch.countDown();
            });
        }
        latch.await();

        List<Long> oriPrice = list.stream().map(l -> l.getOriPrice()).collect(Collectors.toList());
        List<Long> oriVat = list.stream().map(l -> l.getOriVat()).collect(Collectors.toList());
        Collections.sort(oriPrice);
        Collections.sort(oriVat);


        assertThat(oriPrice.get(0)).isEqualTo(10000L);
        assertThat(oriVat.get(0)).isEqualTo(4000L);

    }

}

