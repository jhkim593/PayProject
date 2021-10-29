package com.sparrow.pay.controller;

import com.sparrow.pay.config.Response.DefaultResponseVO;
import com.sparrow.pay.config.Response.ResponseMessage;
import com.sparrow.pay.dto.CancelPayRequestDto;
import com.sparrow.pay.dto.PayRequestDto;
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
    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    public ResponseEntity pay(@RequestBody @Valid PayRequestDto requestDto) throws Exception {

        return new ResponseEntity(DefaultResponseVO.res(ResponseMessage.PAY_SUCCESS, true, payService.createPay(requestDto).getPayId()), HttpStatus.CREATED);

    }

    @PostMapping("/pay/cancel")
    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    public ResponseEntity cancelPay(@RequestBody @Valid CancelPayRequestDto requestDto) {

        return new ResponseEntity(DefaultResponseVO.res(ResponseMessage.PAY_CANCEL_SUCCESS, true, payService.createCancelPay(requestDto)), HttpStatus.CREATED);

    }

    @GetMapping("/pay/{id}")
    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    public ResponseEntity findPay(@PathVariable String id) throws Exception {

        return new ResponseEntity(DefaultResponseVO.res(ResponseMessage.PAY_FIND_SUCCESS, true, payService.findPay(id)), HttpStatus.OK);

    }
}
