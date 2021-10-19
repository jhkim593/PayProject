package com.sparrow.pay.exception;

public class PayNotFoundException extends RuntimeException {
    public PayNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
    public PayNotFoundException(String msg) {
        super(msg);
    }
    public PayNotFoundException() {
        super();
    }
}