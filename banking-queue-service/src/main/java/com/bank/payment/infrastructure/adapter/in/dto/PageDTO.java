package com.bank.payment.infrastructure.adapter.in.dto;

import com.bank.payment.domain.model.Page;

import java.util.List;

public record PageDTO<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
    public static <T> Page<T> of(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages) {
        return new Page<>(content, pageNumber, pageSize, totalElements, totalPages);
    }
}
