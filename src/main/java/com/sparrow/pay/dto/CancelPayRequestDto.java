package com.sparrow.pay.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelPayRequestDto {

    private String payId;
    private Long cancelPrice;
    private Long vat;

}
