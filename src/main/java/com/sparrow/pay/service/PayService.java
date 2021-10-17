package com.sparrow.pay.service;

import com.sparrow.pay.entity.Pay;
import com.sparrow.pay.repository.PayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayService {

    private final PayRepository payRepository;

    @Transactional
    public void createCard(String name){
        payRepository.save(new Pay());
    }

}
