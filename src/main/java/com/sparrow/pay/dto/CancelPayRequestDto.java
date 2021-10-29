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
    @Min(1)
    private Long cancelPrice;

    @Min(1)
    private Long vat;

}
