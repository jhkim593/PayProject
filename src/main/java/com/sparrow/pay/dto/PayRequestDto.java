package com.sparrow.pay.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;


@Getter
@Setter
public class PayRequestDto {

    @NotBlank
    @Pattern(regexp = "[0-9]{10,16}")
    private String cardNum;

    @NotBlank
    @Pattern(regexp = "[0-9]{4}")
    private String expirationDate;

    @NotBlank
    @Pattern(regexp = "[0-9]{3}")
    private String cvc;

    @Min(0)@Max(12)
    @NotNull
    private int installmentMonth;

    @Min(100)@Max(1000000000)
    @NotNull
    private Long price;

    private Long vat;


}
