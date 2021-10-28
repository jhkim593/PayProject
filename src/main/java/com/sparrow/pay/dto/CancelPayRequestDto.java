package com.sparrow.pay.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class CancelPayRequestDto {

    @NotBlank
    private String payId;

    @NotNull
    private Long cancelPrice;

    private Long vat;

}
