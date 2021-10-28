package com.sparrow.pay.service;

import com.sparrow.pay.dto.*;
import com.sparrow.pay.entity.Pay;
import com.sparrow.pay.exception.ExceedCancelPayException;
import com.sparrow.pay.exception.ExceedVatException;
import com.sparrow.pay.exception.PayNotFoundException;
import com.sparrow.pay.exception.VatExceedPriceException;
import com.sparrow.pay.repository.PayRepository;
import com.sparrow.pay.util.AES256Util;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayService {



    @Value("${spring.aes}")
    private String key;

    private String encStr = "";

    private final PayRepository payRepository;

//    public String dd(PayRequestDto requestDto)throws Exception{
//        PayResponseDto pay = createPay(requestDto);
//        pay.getData().substring(103,403).
//    }

    @Transactional
    public PayResponseDto createPay(PayRequestDto requestDto) throws UnsupportedEncodingException, EncoderException, InvalidAlgorithmParameterException,
            NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        //기능 구분
        String type = String.format("%-10s", "PAYMENT");

        //관리번호
        String payId = "";
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            payId += String.valueOf(random.nextInt(10));
        }
        //카드번호
        String cardNum = String.format("%-20s", requestDto.getCardNum());

        //할부 개월 수
        String installmentMonth = String.format("%02d", requestDto.getInstallmentMonth());

        //유효기간
        String expirationDate = String.format("%-4s", requestDto.getExpirationDate());

        //cvc
        String cvc = String.format("%-3s", requestDto.getCvc());

        //거래 금액
        String price = String.format("%10d", requestDto.getPrice());

        //부가가치세
        String vat = null;
        if (requestDto.getVat() == null) {
            vat = String.format("%010d", Math.round(requestDto.getPrice() / (double) 11));
        } else {
            vat = String.format("%010d", requestDto.getVat());
        }
        String oriPayId = String.format("%20s", "");

        //카드번호,유효기간,cvc 데이터 암호화
        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();
        String temp = requestDto.getCardNum() + "_" + String.valueOf(requestDto.getExpirationDate()) + "_" + requestDto.getCvc();

        encStr = codec.encode(aes256.aesEncode(temp));

        String cardInfo = String.format("%-300s", encStr);


        String extra = String.format("%47s", "");

        String data = " 446" + type + payId + cardNum + installmentMonth + expirationDate + cvc + price + vat + oriPayId + cardInfo + extra;

        payRepository.save(Pay.createPay(data, null, payId));
        return new PayResponseDto(payId, data);
    }



    @Transactional
    public CancelPayResponseDto createCancelPay(CancelPayRequestDto requestDto) throws Exception {

        Pay pay = payRepository.findByPayId(requestDto.getPayId()).orElseThrow(PayNotFoundException::new);
        String oriData = pay.getData();

        Long oriPrice = Long.valueOf(oriData.substring(63, 73).trim());
        Long oriVat = Long.valueOf(oriData.substring(73, 83));
        List<Long> priceList = new ArrayList<>();
        List<Long> vatList = new ArrayList<>();

        //결제취소 데이터 값
        pay.getCancelPayList().stream().forEach(c -> {
            String temp = c.getData();
            priceList.add(Long.valueOf(temp.substring(63, 73).trim()));
            vatList.add(Long.valueOf(temp.substring(73, 83)));

        });
        for (Long price : priceList) {
            oriPrice -= price;
        }
        for (Long vat : vatList) {
            oriVat -= vat;
        }

        Long cancelPrice = requestDto.getCancelPrice();
        Long cancelVat = null;

        //부가세 값 설정
        if (requestDto.getVat() == null) {
            cancelVat = Math.round(requestDto.getCancelPrice() / (double) 11);
        } else {
            cancelVat = requestDto.getVat();
        }

        //결제금액 < 취소금액
        if (oriPrice < cancelPrice) {
            throw new ExceedCancelPayException();
        }

        //결제부가세 < 취소 부가세
        if(requestDto.getVat()==null&&oriPrice.equals(cancelPrice)&&cancelVat>oriVat){
            oriVat=0L;
            oriPrice=0L;

        }
        else {
            if (oriVat < cancelVat) {
                throw new ExceedVatException();
            }
            oriPrice -= cancelPrice;
            oriVat -= cancelVat;
        }


        //결제금액 <부가세
        if (oriPrice < oriVat) {
            throw new VatExceedPriceException();
        }
        //기능 구분
        String type = String.format("%-10s", "CANCEL");

        //관리번호
        String payId = "";
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            payId += String.valueOf(random.nextInt(10));
        }

        //카드번호
        String cardNum = oriData.substring(34, 54);

        //할부 개월 수
        String installmentMonth = String.format("%02d", 00);

        //유효기간
        String expirationDate = oriData.substring(56, 60);

        //cvc
        String cvc = oriData.substring(60, 63);

        //취소금액
        String price = String.format("%10d", cancelPrice);

        //취소 부가세
        String vat = String.format("%010d", cancelVat);

        //원거래 관리번호
        String oriPayId = String.format("%-20s", oriData.substring(14, 34));

        //암호화 데이터
        String cardInfo = oriData.substring(103, 403);

        //예비
        String extra = String.format("%47s", "");

        //String data
        String data = " 446" + type + payId + cardNum + installmentMonth + expirationDate + cvc + price + vat + oriPayId + cardInfo + extra;

        payRepository.save(Pay.createPay(data, pay, payId));
//        System.out.println("현재금액:"+oriPrice+" "+"현재부가세"+oriVat);
//        System.out.println();
        return new CancelPayResponseDto(payId,pay.getPayId(),data,oriPrice,oriVat);
    }

    /**
     * 결제 정보 조회
     * @param payId 관리 번호
     */

    @Transactional
    public PayInfoDto findPay(String payId) throws UnsupportedEncodingException, DecoderException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException ,PayNotFoundException{
        Pay pay = payRepository.findByPayId(payId).orElseThrow(PayNotFoundException::new);
        String data = pay.getData();

        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();

        //카드 정보 복호화
        String[] cardInfo = aes256.aesDecode(codec.decode(data.substring(103, 403).trim())).split("_");

        //카드번호 앞6자리 뒤3자리를 제외한 나머지 마스킹
        String cardNum = cardInfo[0];

        cardNum= cardNum.substring(0, 6) + "*".repeat(cardNum.length()-9) + cardNum.substring(cardNum.length() - 3);

        //결제,취소 구분
        String type = data.substring(4, 14).trim();

        //결제,취소금액
        Long price = Long.valueOf(data.substring(63, 73).trim());

        //부가가치세
        Long vat = Long.valueOf(data.substring(73, 83));

        return new PayInfoDto(payId, new CardInfoDto(cardNum,cardInfo[1],cardInfo[2])
                ,type,new PriceInfoDto(price,vat));
    }

}