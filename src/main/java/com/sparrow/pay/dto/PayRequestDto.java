package com.sparrow.pay.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;


@Getter
@Setter
public class PayRequestDto {

    @NotBlank
    @Pattern(regexp = "[0-9]{10,16}" ,message = "10~16자리 숫자를 입력해주세요.")
    private String cardNum;

    @NotBlank
    @Pattern(regexp = "[0-9]{4}",message = "4자리 숫자를 MMYY 형식으로 입력해주세요.")
    private String expirationDate;

    @NotBlank
    @Pattern(regexp = "[0-9]{3}",message = "3자리 숫자를 입력해주세요.")
    private String cvc;

    @Min(0)@Max(12)
    @NotNull
    private Integer installmentMonth;

    @Min(100)@Max(1000000000)
    @NotNull
    private Long price;

    @Min(1)
    private Long vat;


}
