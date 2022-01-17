package com.cydeo.payment.exception;

public class ClassNotInstantiatedException extends Exception {
    public ClassNotInstantiatedException(String message, Throwable cause){
        super(message, cause);
    }
}
