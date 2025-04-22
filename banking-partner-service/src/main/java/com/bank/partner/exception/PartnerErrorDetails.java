package com.bank.partner.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;
    private String path;
    private int status;

    public static PartnerErrorDetails of(String message, String details, String path, int status) {
        return new PartnerErrorDetails(
                LocalDateTime.now(),
                message,
                details,
                path,
                status
        );
    }
}
