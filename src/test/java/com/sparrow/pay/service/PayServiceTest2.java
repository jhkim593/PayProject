package com.sparrow.pay.service;

import com.sparrow.pay.dto.CancelPayRequestDto;
import com.sparrow.pay.dto.CancelPayResponseDto;
import com.sparrow.pay.dto.PayRequestDto;
import com.sparrow.pay.dto.PayResponseDto;
import com.sparrow.pay.exception.ExceedCancelPayException;
import com.sparrow.pay.exception.ExceedVatException;
import com.sparrow.pay.exception.VatExceedPriceException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class PayServiceTest2 {

    @Autowired
    PayService payService;

    @Autowired
    EntityManager em;

    @Test
    public void case1()throws Exception {

        //1.결제
        PayRequestDto payRequestDto = new PayRequestDto();
        payRequestDto.setCardNum("1234567890123456");
        payRequestDto.setExpirationDate("1125");
        payRequestDto.setCvc("777");
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
}
