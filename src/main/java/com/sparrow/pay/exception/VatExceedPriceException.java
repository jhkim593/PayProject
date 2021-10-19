package com.sparrow.pay.exception;

public class VatExceedPriceException extends RuntimeException {
    public VatExceedPriceException(String msg, Throwable t) {
        super(msg, t);
    }
    public VatExceedPriceException(String msg) {
        super(msg);
    }
    public VatExceedPriceException() {
        super();
    }
}