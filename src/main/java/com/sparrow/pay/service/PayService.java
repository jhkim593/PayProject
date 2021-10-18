package com.sparrow.pay.service;

import com.sparrow.pay.dto.PayRequestDto;
import com.sparrow.pay.dto.PayResponseDto;
import com.sparrow.pay.entity.Pay;
import com.sparrow.pay.repository.PayRepository;
import com.sparrow.pay.util.AES256Util;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayService {

    private static String key = "aes256-testingKey";
    private String encStr = "";

    private final PayRepository payRepository;

    @Transactional
    public PayResponseDto createPay(PayRequestDto requestDto) throws UnsupportedEncodingException, EncoderException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        PayResponseDto responseDto = getStringData(requestDto);
        payRepository.save(Pay.createPay(responseDto.getData(), null));
        return responseDto;
    }

    public PayResponseDto getStringData(PayRequestDto requestDto) throws UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, EncoderException {
        Random random=new Random();
        //기능 구분
        String payment = String.format("%-10s",requestDto.getFunc());

        //관리번호
        String id="";
        for(int i=0;i<20;i++){
            id+=String.valueOf(random.nextInt(10));
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
        String vat=null;
        if(requestDto.getVat()==null) {
            vat = String.format("%010d", Math.round(requestDto.getPrice() / (double) 11));
        }else{
            vat = String.format("%010d", requestDto.getVat());
        }
        String oriPayId=String.format("%20s","");

        //카드번호,유효기간,cvc 데이터 암호화
        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();
        String temp = requestDto.getCardNum()+"_"+ String.valueOf(requestDto.getExpirationDate())+"_"+ requestDto.getCvc();

        encStr = codec.encode(aes256.aesEncode(temp));

        String cardInfo = String.format("%-300s", encStr);


        String extra = String.format("%47s", "");

        String data=" 446"+payment+id+cardNum+installmentMonth+expirationDate+cvc+price+vat+oriPayId+cardInfo+extra;

        PayResponseDto responseDto=new PayResponseDto(id,data);

        return responseDto;
    }

}
