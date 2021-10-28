package com.sparrow.pay.controller;

import com.sparrow.pay.config.Response.DefaultResponseVO;
import com.sparrow.pay.config.Response.ResponseMessage;
import com.sparrow.pay.dto.CancelPayRequestDto;
import com.sparrow.pay.dto.PayRequestDto;
import com.sparrow.pay.exception.ExceedCancelPayException;
import com.sparrow.pay.exception.ExceedVatException;
import com.sparrow.pay.exception.PayNotFoundException;
import com.sparrow.pay.exception.VatExceedPriceException;
import com.sparrow.pay.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;
    
    @PostMapping("/pay")
    @CrossOrigin(origins = "http://localhost:3000",allowedHeaders ="*")
    public ResponseEntity pay(@RequestBody @Valid PayRequestDto requestDto){
        try {
            return new ResponseEntity(DefaultResponseVO.res(ResponseMessage.PAY_SUCCESS,true,payService.createPay(requestDto).getPayId()),HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity(DefaultResponseVO.res( ResponseMessage.PAY_FAIL,false),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/pay/cancel")
    @CrossOrigin(origins = "http://localhost:3000",allowedHeaders ="*")
    public ResponseEntity cancelPay(@RequestBody @Valid CancelPayRequestDto requestDto){
        try {
            return new ResponseEntity(DefaultResponseVO.res(ResponseMessage.PAY_CANCEL_SUCCESS,true,payService.createCancelPay(requestDto)), HttpStatus.CREATED);
        }catch (PayNotFoundException e){
            return new ResponseEntity(DefaultResponseVO.res(ResponseMessage.PAY_NOT_FOUND,false), HttpStatus.NOT_FOUND);
        } catch (ExceedCancelPayException e){
            return new ResponseEntity(DefaultResponseVO.res(ResponseMessage.PAY_CANCEL_EXCEED,false), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ExceedVatException e){
            return new ResponseEntity(DefaultResponseVO.res( ResponseMessage.PAY_CANCEL_VAT_EXCEED,false), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (VatExceedPriceException e){
            return new ResponseEntity(DefaultResponseVO.res( ResponseMessage.PAY_CANCEL_VAT_PAY_EXCEED,false), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            return new ResponseEntity( DefaultResponseVO.res(ResponseMessage.PAY_CANCEL_FAIL,false) ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pay/{id}")
    @CrossOrigin(origins = "http://localhost:3000",allowedHeaders ="*")
    public ResponseEntity findPay(@PathVariable String id){
        try{
            return new ResponseEntity(DefaultResponseVO.res(ResponseMessage.PAY_FIND_SUCCESS,true,payService.findPay(id)), HttpStatus.OK);
        }catch (PayNotFoundException e){
            return new ResponseEntity( DefaultResponseVO.res(ResponseMessage.PAY_NOT_FOUND,false), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(DefaultResponseVO.res( ResponseMessage.PAY_FIND_FAIL,false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
