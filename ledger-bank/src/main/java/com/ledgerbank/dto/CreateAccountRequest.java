
package com.ledgerbank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank(message = "Account number cannot be blank.")
        String accountNumber,

        @NotBlank(message = "Owner name cannot be blank.")
        String ownerName,

        @NotNull(message = "Initial balance cannot be null.")
        @PositiveOrZero(message = "Initial balance cannot be negative.")
        BigDecimal initialBalance
) {}
