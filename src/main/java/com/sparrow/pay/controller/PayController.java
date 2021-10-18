package com.sparrow.pay.controller;

import com.sparrow.pay.dto.PayRequestDto;
import com.sparrow.pay.dto.PayResponseDto;
import com.sparrow.pay.service.PayService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.EncoderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    @PostMapping("/pay")
    public ResponseEntity pay(@RequestBody PayRequestDto requestDto){
        requestDto.setFunc("PAYMENT");
        try {
            return new ResponseEntity( payService.createPay(requestDto), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity( "결제 오류", HttpStatus.CREATED);
        }

    }
    
}
