package com.sparrow.pay.config.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DefaultResponseVO<T> {

    private String responseMessage;
    private Boolean isSuccess;
    private T data;

    public DefaultResponseVO(String responseMessage, Boolean isSuccess) {
        this.responseMessage = responseMessage;
        this.isSuccess = isSuccess;
    }

    public static<T> DefaultResponseVO<T> res(final String responseMessage, final Boolean isSuccess) {
        return res(responseMessage, isSuccess, null);
    }

    public static<T> DefaultResponseVO<T> res(final String responseMessage, final Boolean isSuccess, final T t) {
        return DefaultResponseVO.<T>builder()
                .data(t)
                .responseMessage(responseMessage)
                .isSuccess(isSuccess)
                .build();
    }

}