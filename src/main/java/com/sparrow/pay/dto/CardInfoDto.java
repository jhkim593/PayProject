package com.sparrow.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardInfoDto {
    private String cardNum;
    private String expirationDate;
    private String cvc;
}
