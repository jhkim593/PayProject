package com.sparrow.pay.service;

import com.sparrow.pay.dto.CancelPayRequestDto;
import com.sparrow.pay.dto.PayRequestDto;
import com.sparrow.pay.dto.PayResponseDto;
import com.sparrow.pay.repository.PayRepository;
import com.sparrow.pay.util.AES256Util;
import org.apache.commons.codec.net.URLCodec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class PayServiceTest {

    @Mock
    PayRepository payRepository;

    @InjectMocks
    PayService payService;

    private static String key = "aes256-testingKey";

    /**
     * 부가세 있을 때 결제**/
    @Test
    public void createPayTestWithVAT()throws Exception{

        //given
        PayRequestDto requestDto=new PayRequestDto();
        requestDto.setCardNum(1234567890123456L);    //카드번호
        requestDto.setExpirationDate(1125);          //유효기간
        requestDto.setCvc(777);                      //cvc
        requestDto.setInstallmentMonth(0);           //할부 개월수
        requestDto.setPrice(110000L);                //거래금액
        requestDto.setVat(10000L);                   //부가세

        //when
        PayResponseDto responseDto = payService.createPay(requestDto);
        String id = responseDto.getPayId();
        String data = responseDto.getData();
//        AES256Util aes256 = new AES256Util(key);
//        URLCodec codec = new URLCodec();
//
//        String[] cardInfo = codec.decode(aes256.aesDecode(responseDto.getData().substring(103,403)).trim().split("_");


        //then
        assertThat(data.substring(0,4)).isEqualTo(" 446");                           //데이터 길이
        assertThat(data.substring(4,14)).isEqualTo("PAYMENT   ");                    //데이터 구분
        assertThat(data.substring(14,34)).isEqualTo(id);                             //관리번호
        assertThat(data.substring(34,54)).isEqualTo(requestDto.getCardNum()+"    "); //카드번호
        assertThat(data.substring(54,56)).isEqualTo("00");                           //할부개월수
        assertThat(data.substring(56,60)).isEqualTo("1125");                         //유효기간
        assertThat(data.substring(60,63)).isEqualTo("777");                          //cvc
        assertThat(data.substring(63,73)).isEqualTo("    110000");                   //거래금액
        assertThat(data.substring(73,83)).isEqualTo("0000010000");                   //부가세
        assertThat(data.substring(83,103)).isEqualTo("                    ");
//        assertThat(cardInfo[0]).isEqualTo("1234567890123456");
//        assertThat(cardInfo[1]).isEqualTo("1125");
//        assertThat(cardInfo[2]).isEqualTo("777");
        assertThat(data.substring(403,450).trim().length()).isEqualTo(0);


    }

    /**
     * 부가세 없을때 결제**/
    @Test
    public void createPayTest()throws Exception{
        //given
        PayRequestDto requestDto=new PayRequestDto();
        requestDto.setCardNum(1234567890123456L);    //카드번호
        requestDto.setExpirationDate(1125);          //유효기간
        requestDto.setCvc(777);                      //cvc
        requestDto.setInstallmentMonth(0);           //할부 개월수
        requestDto.setPrice(110000L);                //거래금액
        requestDto.setVat(null);                     //부가세 null


        //when
        PayResponseDto responseDto = payService.createPay(requestDto);
        String data = responseDto.getData();
        String id = responseDto.getPayId();
        //then
        assertThat(data.substring(0,4)).isEqualTo(" 446");                           //데이터 길이
        assertThat(data.substring(4,14)).isEqualTo("PAYMENT   ");                    //데이터 구분
        assertThat(data.substring(14,34)).isEqualTo(id);                             //관리번호
        assertThat(data.substring(34,54)).isEqualTo(requestDto.getCardNum()+"    "); //카드번호
        assertThat(data.substring(54,56)).isEqualTo("00");                           //할부개월수
        assertThat(data.substring(56,60)).isEqualTo("1125");                         //유효기간
        assertThat(data.substring(60,63)).isEqualTo("777");                          //cvc
        assertThat(data.substring(63,73)).isEqualTo("    110000");                   //거래금액
        assertThat(data.substring(73,83)).isEqualTo("0000010000");                   //부가세
        assertThat(data.substring(83,103)).isEqualTo("                    ");


    }
    @Test
    public void createCancelPay()throws Exception{
        //given
        CancelPayRequestDto cancelPayRequestDto=new CancelPayRequestDto();
        cancelPayRequestDto.setPayId();
        cancelPayRequestDto.setCancelPrice();
        cancelPayRequestDto.setVat();

        //when
        PayResponseDto cancelPay = payService.createCancelPay(cancelPayRequestDto);

        //then

    }

}