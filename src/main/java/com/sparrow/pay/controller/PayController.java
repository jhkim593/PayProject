package com.sparrow.pay.controller;

import com.sparrow.pay.dto.CancelPayRequestDto;
import com.sparrow.pay.dto.PayRequestDto;
import com.sparrow.pay.dto.PayResponseDto;
import com.sparrow.pay.exception.ExceedCancelPayException;
import com.sparrow.pay.exception.ExceedVatException;
import com.sparrow.pay.exception.PayNotFoundException;
import com.sparrow.pay.exception.VatExceedPriceException;
import com.sparrow.pay.service.PayService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.EncoderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;
    
    @PostMapping("/pay")
    public ResponseEntity pay(@RequestBody @Valid PayRequestDto requestDto){
        try {
            return new ResponseEntity( payService.createPay(requestDto).getPayId(), HttpStatus.CREATED);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity( "결제 오류", HttpStatus.CREATED);
        }
    }

    @PostMapping("/pay/cancel")
    public ResponseEntity cancelPay(@RequestBody @Valid CancelPayRequestDto requestDto){
        try {
            return new ResponseEntity( payService.createCancelPay(requestDto).getPayId(), HttpStatus.CREATED);
        }catch (PayNotFoundException e){
            return new ResponseEntity( "기존 결제 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        } catch (ExceedCancelPayException e){
            return new ResponseEntity( "취소금액이 결제금액을 초과했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ExceedVatException e){
            return new ResponseEntity( "취소부가세가 결제부가세를 초과했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (VatExceedPriceException e){
            return new ResponseEntity( "부가세가 결제금액을 초과했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            return new ResponseEntity( "결제 취소 오류", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pay/{id}")
    public ResponseEntity findPay(@PathVariable String id){
        try{
            return new ResponseEntity(payService.findPay(id), HttpStatus.OK);
        }catch (PayNotFoundException e){
            return new ResponseEntity( "기존 결제 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity( "결제 정보 조회 오류", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
