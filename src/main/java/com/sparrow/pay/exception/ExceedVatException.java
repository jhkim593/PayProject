package com.sparrow.pay.exception;

public class ExceedVatException extends RuntimeException {
    public ExceedVatException(String msg, Throwable t) {
        super(msg, t);
    }
    public ExceedVatException(String msg) {
        super(msg);
    }
    public ExceedVatException() {
        super();
    }
}