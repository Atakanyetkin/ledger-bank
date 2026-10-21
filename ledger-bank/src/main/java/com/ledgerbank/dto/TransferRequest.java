package com.ledgerbank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank(message = "Source account number cannot be blank.")
        String fromAccountNumber,

        @NotBlank(message = "Destination account number cannot be blank.")
        String toAccountNumber,

        @NotNull(message = "Amount cannot be null.")
        @Positive(message = "Amount must be greater than zero.")
        BigDecimal amount
) {
}
