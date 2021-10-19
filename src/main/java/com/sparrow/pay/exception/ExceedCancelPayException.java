package com.sparrow.pay.exception;

public class ExceedCancelPayException extends RuntimeException {
    public ExceedCancelPayException(String msg, Throwable t) {
        super(msg, t);
    }
    public ExceedCancelPayException(String msg) {
        super(msg);
    }
    public ExceedCancelPayException() {
        super();
    }
}