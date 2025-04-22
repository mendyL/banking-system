package com.bank.partner.exception;

public class PartnerNotFoundException extends RuntimeException {
    public PartnerNotFoundException(String message) {
        super(message);
    }
}
