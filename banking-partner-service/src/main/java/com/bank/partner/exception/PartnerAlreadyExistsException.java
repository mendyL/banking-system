package com.bank.partner.exception;

public class PartnerAlreadyExistsException  extends RuntimeException  {
    public PartnerAlreadyExistsException(String message) {
        super(message);
    }
}
