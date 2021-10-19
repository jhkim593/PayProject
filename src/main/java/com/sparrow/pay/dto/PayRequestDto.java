package com.sparrow.pay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayRequestDto {
    private long cardNum;
    private int expirationDate;
    private int cvc;
    private int installmentMonth;
    private Long price;
    private Long vat;


}
