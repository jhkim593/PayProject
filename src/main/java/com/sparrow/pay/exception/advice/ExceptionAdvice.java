package com.sparrow.pay.exception.advice;

import com.sparrow.pay.config.Response.DefaultResponseVO;
import com.sparrow.pay.config.Response.ResponseMessage;
import com.sparrow.pay.exception.ExceedCancelPayException;
import com.sparrow.pay.exception.ExceedVatException;
import com.sparrow.pay.exception.PayNotFoundException;
import com.sparrow.pay.exception.VatExceedPriceException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        return new ResponseEntity(DefaultResponseVO.res(errorMessage,false), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PayNotFoundException.class)
    protected ResponseEntity countryNotFoundException(HttpServletRequest request, PayNotFoundException e){
        return new ResponseEntity(DefaultResponseVO.res(ResponseMessage.PAY_NOT_FOUND,false), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExceedCancelPayException.class)
    protected ResponseEntity exceedCancelPayException(HttpServletRequest request, ExceedCancelPayException e){
        return new ResponseEntity(DefaultResponseVO.res(ResponseMessage.PAY_CANCEL_EXCEED,false), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExceedVatException.class)
    protected ResponseEntity exceedVatException(HttpServletRequest request, ExceedVatException e){
        return new ResponseEntity(DefaultResponseVO.res( ResponseMessage.PAY_CANCEL_VAT_EXCEED,false), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(VatExceedPriceException.class)
    protected ResponseEntity vatExceedPriceException(HttpServletRequest request, VatExceedPriceException e){
        return new ResponseEntity(DefaultResponseVO.res( ResponseMessage.PAY_CANCEL_VAT_PAY_EXCEED,false), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({UnsupportedEncodingException.class, InvalidAlgorithmParameterException.class,
            NoSuchPaddingException.class, IllegalBlockSizeException.class, NoSuchAlgorithmException.class, BadPaddingException.class,
            InvalidKeyException.class,EncoderException.class,DecoderException.class})
    protected ResponseEntity aesException(HttpServletRequest request){
        return new ResponseEntity(DefaultResponseVO.res("암호화시 오류가 발생했습니다.",false), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity defaultException(HttpServletRequest request, Exception e){
        e.printStackTrace();
        return new ResponseEntity(DefaultResponseVO.res("오류가 발생했습니다.",false), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //상태코드?
    //format exception처리
    //결제 취소시 data를 return할것인가?

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity invaException(HttpServletRequest request, Exception e){
        e.printStackTrace();
        return new ResponseEntity(DefaultResponseVO.res("11오류가 발생했습니다.",false), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
