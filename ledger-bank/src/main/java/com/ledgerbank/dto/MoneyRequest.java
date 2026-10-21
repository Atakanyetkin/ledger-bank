package com.ledgerbank.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MoneyRequest(
        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amout must be greater than zero")
        BigDecimal amount
) {}