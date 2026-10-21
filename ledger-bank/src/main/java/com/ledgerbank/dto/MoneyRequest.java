package com.ledgerbank.dto;

import java.math.BigDecimal;

public record MoneyRequest(BigDecimal amount) {
}