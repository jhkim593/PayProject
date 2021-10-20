package com.sparrow.pay.service;

import com.sparrow.pay.dto.CancelPayRequestDto;
import com.sparrow.pay.dto.PayInfoDto;
import com.sparrow.pay.dto.PayRequestDto;
import com.sparrow.pay.dto.PayResponseDto;
import com.sparrow.pay.entity.Pay;
import com.sparrow.pay.exception.ExceedCancelPayException;
import com.sparrow.pay.exception.ExceedVatException;
import com.sparrow.pay.exception.PayNotFoundException;
import com.sparrow.pay.exception.VatExceedPriceException;
import com.sparrow.pay.repository.PayRepository;
import com.sparrow.pay.util.AES256Util;
import org.apache.commons.codec.net.URLCodec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class PayServiceTest {

    @Mock
    PayRepository payRepository;

    @InjectMocks
    PayService payService;

    private static String key = "aes256-testingKey";

    /**
     * 부가세 있을 때 결제 **/
    @Test
    public void createPayTestWithVAT()throws Exception{

        //given
        PayRequestDto requestDto=new PayRequestDto();
        requestDto.setCardNum("1234567890123456");    //카드번호
        requestDto.setExpirationDate("1125");          //유효기간
        requestDto.setCvc("777");                      //cvc
        requestDto.setInstallmentMonth(0);           //할부 개월수
        requestDto.setPrice(110000L);                //거래금액
        requestDto.setVat(10000L);                   //부가세

        //when
        PayResponseDto responseDto = payService.createPay(requestDto);
        String id = responseDto.getPayId();
        String data = responseDto.getData();
        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();

        String[] cardInfo = aes256.aesDecode(codec.decode(responseDto.getData().substring(103, 403).trim())).split("_");

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
        assertThat(cardInfo[0]).isEqualTo("1234567890123456");
        assertThat(cardInfo[1]).isEqualTo("1125");
        assertThat(cardInfo[2]).isEqualTo("777");
        assertThat(data.substring(403).length()).isEqualTo(47);


    }

    /**
     * 부가세 없을때 결제 **/
    @Test
    public void createPayTest()throws Exception{
        //given
        PayRequestDto requestDto=new PayRequestDto();
        requestDto.setCardNum("1234567890123456");    //카드번호
        requestDto.setExpirationDate("1125");          //유효기간
        requestDto.setCvc("777");                      //cvc
        requestDto.setInstallmentMonth(0);           //할부 개월수
        requestDto.setPrice(110000L);                //거래금액
        requestDto.setVat(null);                     //부가세 null

        //when
        PayResponseDto responseDto = payService.createPay(requestDto);
        String data = responseDto.getData();
        String id = responseDto.getPayId();
        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();

        String[] cardInfo = aes256.aesDecode(codec.decode(responseDto.getData().substring(103, 403).trim())).split("_");


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
        assertThat(cardInfo[0]).isEqualTo("1234567890123456");
        assertThat(cardInfo[1]).isEqualTo("1125");
        assertThat(cardInfo[2]).isEqualTo("777");
        assertThat(data.substring(403).length()).isEqualTo(47);


    }

    /**
     * 결제 취소
     * 취소금액 , vat 모두 정상입력 **/
    @Test
    public void createCancelPay()throws Exception{
        //given
        CancelPayRequestDto requestDto = new CancelPayRequestDto();
        requestDto.setPayId("57441386610808376609");
        requestDto.setVat(100L);
        requestDto.setCancelPrice(1200L);

        String temp=" 446PAYMENT   574413866108083766091234567890123456    001125777    1100000000010000                    lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        given(payRepository.findByPayId(anyString())).willReturn(Optional.of(Pay.createPay(temp,null,"57441386610808376609")));
        //when
        PayResponseDto cancelPay = payService.createCancelPay(requestDto);
        String payId = cancelPay.getPayId();
        String data = cancelPay.getData();
        //then
        assertThat(data.substring(0,4)).isEqualTo(" 446");                           //데이터 길이
        assertThat(data.substring(4,14)).isEqualTo("CANCEL    ");                    //데이터 구분
        assertThat(data.substring(14,34)).isEqualTo(payId);                             //관리번호
        assertThat(data.substring(34,54)).isEqualTo("1234567890123456    ");         //카드번호
        assertThat(data.substring(54,56)).isEqualTo("00");                           //할부개월수
        assertThat(data.substring(56,60)).isEqualTo("1125");                         //유효기간
        assertThat(data.substring(60,63)).isEqualTo("777");                          //cvc
        assertThat(data.substring(63,73)).isEqualTo("      1200");                   //거래금액
        assertThat(data.substring(73,83)).isEqualTo("0000000100");                   //부가세
        assertThat(data.substring(83,103)).isEqualTo("57441386610808376609");        //원 관리번호

    }
    /**
     * 결제 취소
     * 취소금액 > 결제금액 **/
    @Test
    public void createCancelPayExceedCancelPrice()throws Exception{
        //given
        CancelPayRequestDto requestDto = new CancelPayRequestDto();
        requestDto.setPayId("57441386610808376609");
        requestDto.setVat(0L);
        requestDto.setCancelPrice(1200000L);

        //when
        String temp=" 446PAYMENT   574413866108083766091234567890123456    001125777    1100000000010000                    lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        given(payRepository.findByPayId(anyString())).willReturn(Optional.of(Pay.createPay(temp,null,"57441386610808376609")));

        //then
        assertThatThrownBy(()->payService.createCancelPay(requestDto)).isInstanceOf(ExceedCancelPayException.class);

    }

    /**
     * 결제 취소
     * 취소 부가세 > 결제 부가세 **/
    @Test
    public void createCancelPayExceedVat()throws Exception{

        //given
        CancelPayRequestDto requestDto = new CancelPayRequestDto();
        requestDto.setPayId("57441386610808376609");
        requestDto.setVat(10001L);
        requestDto.setCancelPrice(100000L);

        //when
        String temp=" 446PAYMENT   574413866108083766091234567890123456    001125777    1100000000010000                    lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        given(payRepository.findByPayId(anyString())).willReturn(Optional.of(Pay.createPay(temp,null,"57441386610808376609")));

        //then
        assertThatThrownBy(()->payService.createCancelPay(requestDto)).isInstanceOf(ExceedVatException.class);

    }

    /**
     * 결제 취소
     * 결제금액 < 부가세 **/
    @Test
    public void createCancelPayVatExceedPrice()throws Exception{
        //given
        CancelPayRequestDto requestDto = new CancelPayRequestDto();
        requestDto.setPayId("57441386610808376609");
        requestDto.setVat(0L);
        requestDto.setCancelPrice(105000L);

        //when
        String temp=" 446PAYMENT   574413866108083766091234567890123456    001125777    1100000000010000                    lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        given(payRepository.findByPayId(anyString())).willReturn(Optional.of(Pay.createPay(temp,null,"57441386610808376609")));

        //then
        assertThatThrownBy(()->payService.createCancelPay(requestDto)).isInstanceOf(VatExceedPriceException.class);
    }

    /**
     * 결제 취소
     * 취소금액 50000 취소부가세 5000
     * 취소금액 11000 취사부가세 1000
     *
     * 현재
     * 결제금액 49000 결제부가세 4000
     */

    @Test
    public void createCancelPay2()throws Exception{
        //given
        CancelPayRequestDto requestDto = new CancelPayRequestDto();
        requestDto.setPayId("57441386610808376609");
        requestDto.setVat(0L);
        requestDto.setCancelPrice(50000L);

        //when
        String temp1=" 446PAYMENT   574413866108083766091234567890123456    001125777    1100000000010000                    lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        Pay pay = Pay.createPay(temp1, null, "57441386610808376609");
        String temp2=" 446CANCEL    958454518445984482621234567890123456    001125777     50000000000500057441386610808376609lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        String temp3=" 446CANCEL    203106241900488177801234567890123456    001125777     11000000000100057441386610808376609lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        Pay.createPay(temp2, pay, "95845451844598448262");
        Pay.createPay(temp3, pay, "20310624190048817780");
        given(payRepository.findByPayId(anyString())).willReturn(Optional.of(pay));

        //then
        assertThatThrownBy(()->payService.createCancelPay(requestDto)).isInstanceOf(ExceedCancelPayException.class);

    }

    /**
     * 결제 취소
     * 취소금액 50000 취소부가세 5000
     * 취소금액 11000 취사부가세 1000
     *
     * 현재
     * 결제금액 49000 결제부가세 4000
     */

    @Test
    public void createCancelPay3()throws Exception{
        //given
        CancelPayRequestDto requestDto = new CancelPayRequestDto();
        requestDto.setPayId("57441386610808376609");
        requestDto.setVat(5000L);
        requestDto.setCancelPrice(10000L);

        //when
        String temp1=" 446PAYMENT   574413866108083766091234567890123456    001125777    1100000000010000                    lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        Pay pay = Pay.createPay(temp1, null, "57441386610808376609");
        String temp2=" 446CANCEL    958454518445984482621234567890123456    001125777     50000000000500057441386610808376609lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        String temp3=" 446CANCEL    203106241900488177801234567890123456    001125777     11000000000100057441386610808376609lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        Pay.createPay(temp2, pay, "95845451844598448262");
        Pay.createPay(temp3, pay, "20310624190048817780");
        given(payRepository.findByPayId(anyString())).willReturn(Optional.of(pay));

        //then
        assertThatThrownBy(()->payService.createCancelPay(requestDto)).isInstanceOf(ExceedVatException.class);

    }
    /**
     * 결제 취소
     * 취소금액 50000 취소부가세 5000
     * 취소금액 11000 취사부가세 1000
     *
     * 현재
     * 결제금액 49000 결제부가세 4000
     */

    @Test
    public void createCancelPay4()throws Exception{
        //given
        CancelPayRequestDto requestDto = new CancelPayRequestDto();
        requestDto.setPayId("57441386610808376609");
        requestDto.setVat(0L);
        requestDto.setCancelPrice(49000L);

        //when
        String temp1=" 446PAYMENT   574413866108083766091234567890123456    001125777    1100000000010000                    lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        Pay pay = Pay.createPay(temp1, null, "57441386610808376609");
        String temp2=" 446CANCEL    958454518445984482621234567890123456    001125777     50000000000500057441386610808376609lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        String temp3=" 446CANCEL    203106241900488177801234567890123456    001125777     11000000000100057441386610808376609lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        Pay.createPay(temp2, pay, "95845451844598448262");
        Pay.createPay(temp3, pay, "20310624190048817780");
        given(payRepository.findByPayId(anyString())).willReturn(Optional.of(pay));

        //then
        assertThatThrownBy(()->payService.createCancelPay(requestDto)).isInstanceOf(VatExceedPriceException.class);
    }

    /**
     * 결제 정보 조회 **/
    @Test
    public void findPay()throws Exception{
        //given
        String temp=" 446PAYMENT   574413866108083766091234567890123456    001125777    1100000000010000                    lQUrO5X3EKg1B4A8rlN%2F2ReOC8hgQZVqGKr6I9sboPI%3D                                                                                                                                                                                                                                                                                                           ";
        given(payRepository.findByPayId(anyString())).willReturn(Optional.of(Pay.createPay(temp,null,"57441386610808376609")));

        //when
        PayInfoDto pay = payService.findPay("57441386610808376609");

        //then
        assertThat(pay.getPayId()).isEqualTo("57441386610808376609");
        assertThat(pay.getCardInfo().getCardNum()).isEqualTo("123456*******456");
        assertThat(pay.getCardInfo().getExpirationDate()).isEqualTo("1125");
        assertThat(pay.getCardInfo().getCvc()).isEqualTo("777");
        assertThat(pay.getPriceInfo().getPrice()).isEqualTo(110000L);
        assertThat(pay.getPriceInfo().getVat()).isEqualTo(10000L);
        assertThat(pay.getType()).isEqualTo("PAYMENT");
    }
}