package com.sparrow.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayInfoDto {

    private String payId;
    private CardInfoDto cardInfo;
    private String type;
    private PriceInfoDto priceInfo;
}
