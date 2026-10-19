package com.ledgerbank.service;

import com.ledgerbank.model.Account;

/**
 * Coordinates banking operations on accounts.
 */
public class BankingService {

    public void deposit(Account account, java.math.BigDecimal amount) {
        account.deposit(amount);
    }

    public void withdraw(Account account, java.math.BigDecimal amount) {
        account.withdraw(amount);
    }
}