package com.ledgerbank.dto;

import  java.math.BigDecimal;

public record CreateAccountRequest(
        String accountNumber,
        String ownerName,
        BigDecimal initialBalance) {

}
