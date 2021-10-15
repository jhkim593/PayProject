package com.sparrow.pay.service;

import com.sparrow.pay.entity.Card;
import com.sparrow.pay.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;

    @Transactional
    public void createCard(String name){
        cardRepository.save(new Card(null,name));
    }

}
