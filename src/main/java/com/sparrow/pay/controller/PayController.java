package com.sparrow.pay.controller;

import com.sparrow.pay.dto.CancelPayRequestDto;
import com.sparrow.pay.dto.PayRequestDto;
import com.sparrow.pay.dto.PayResponseDto;
import com.sparrow.pay.exception.ExceedCancelPayException;
import com.sparrow.pay.exception.ExceedVatException;
import com.sparrow.pay.exception.VatExceedPriceException;
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
        try {
            return new ResponseEntity( payService.createPay(requestDto), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity( "결제 오류", HttpStatus.CREATED);
        }
    }

    @PostMapping("/pay/cancel")
    public ResponseEntity cancelPay(@RequestBody CancelPayRequestDto requestDto){
        try {
            return new ResponseEntity( payService.createCancelPay(requestDto), HttpStatus.CREATED);
        } catch (ExceedCancelPayException e){
            return new ResponseEntity( "취소금액이 결제금액을 초과했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ExceedVatException e){
            return new ResponseEntity( "취소부가세가 결제부가세를 초과했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (VatExceedPriceException e){
            return new ResponseEntity( "부가세가 결제금액을 초과했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            return new ResponseEntity( "결제 오류", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
