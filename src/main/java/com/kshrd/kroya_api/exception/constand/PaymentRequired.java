package com.kshrd.kroya_api.exception.constand;

public class PaymentRequired extends RuntimeException {
    public PaymentRequired(String message) {
        super(message);
    }
}
