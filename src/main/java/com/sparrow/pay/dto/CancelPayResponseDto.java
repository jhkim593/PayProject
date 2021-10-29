package com.sparrow.pay.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CancelPayResponseDto {
    private String payId;
    private String oriPayId;
    @JsonIgnore
    private String data;
    private Long oriPrice;
    private Long oriVat;
}
