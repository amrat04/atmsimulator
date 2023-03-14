package com.amrat.atmsimulator.exception;

public class ATMTransactionException extends Exception{

    public ATMTransactionException(String message) {
        super(message);
    }

    public ATMTransactionException(Exception e) {
        super(e);
    }

}
