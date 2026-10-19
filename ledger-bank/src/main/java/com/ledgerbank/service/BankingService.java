
package com.ledgerbank.service;

import com.ledgerbank.model.Account;
import com.ledgerbank.repository.AccountRepository;

import java.math.BigDecimal;

/**
 * Coordinates banking operations using the account repository.
 */
public class BankingService {

    private final AccountRepository accountRepository;

    /**
     * Creates the service with a repository dependency.
     *
     * @param accountRepository repository used to store and retrieve accounts
     */
    public BankingService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Creates and stores a new bank account.
     */
    public void createAccount(
            String accountNumber,
            String ownerName,
            BigDecimal initialBalance
    ) {
        Account account = new Account(
                accountNumber,
                ownerName,
                initialBalance
        );

        accountRepository.save(account);
    }

    /**
     * Deposits money into an existing account.
     */
    public void deposit(String accountNumber, BigDecimal amount) {
        Account account = findAccountOrThrow(accountNumber);
        account.deposit(amount);
    }

    /**
     * Withdraws money from an existing account.
     */
    public void withdraw(String accountNumber, BigDecimal amount) {
        Account account = findAccountOrThrow(accountNumber);
        account.withdraw(amount);
    }

    /**
     * Retrieves an account or fails if it does not exist.
     */
    public Account getAccount(String accountNumber) {
        return findAccountOrThrow(accountNumber);
    }

    private Account findAccountOrThrow(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found: " + accountNumber
                ));
    }
}
