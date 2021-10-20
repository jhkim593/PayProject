package com.sparrow.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancelPayResponseDto {
    private String payId;
    private String data;
    private Long oriPrice;
    private Long oriVat;
}
