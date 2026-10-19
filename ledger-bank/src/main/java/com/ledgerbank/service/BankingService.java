
package com.ledgerbank.service;

import com.ledgerbank.model.Account;
import com.ledgerbank.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Objects;
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
        this.accountRepository = Objects.requireNonNull(
                accountRepository,
                "Account repository cannot be null"
        );
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
     * Transfers money between two existing accounts.
     *
     * @param fromAccountNumber account number to transfer money from
     * @param toAccountNumber account number to transfer money to
     * @param amount amount to transfer; must be positive
     */
    public void transfer(
            String fromAccountNumber,
            String toAccountNumber,
            BigDecimal amount
    ) {

        Objects.requireNonNull(
                fromAccountNumber,
                "Sender account number cannot be null."
        );
        Objects.requireNonNull(
                toAccountNumber,
                "Recipient account number cannot be null."
        );

        if (fromAccountNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Sender account number cannot be blank."
            );
        }
        if (toAccountNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Recipient account number cannot be blank."
            );
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException(
                    "Cannot transfer money to the same account."
            );
        }


        Account sender = findAccountOrThrow(fromAccountNumber);
        Account recipient = findAccountOrThrow(toAccountNumber);

        sender.withdraw(amount);
        recipient.deposit(amount);
    }


    /**
     * Retrieves an account or fails if it does not exist.
     */
    public Account getAccount(String accountNumber) {
        return findAccountOrThrow(accountNumber);
    }


    private Account findAccountOrThrow(String accountNumber) {
        Objects.requireNonNull(
                accountNumber,
                "Account number cannot be null."
        );

        if (accountNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Account number cannot be blank."
            );
        }

        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found: " + accountNumber
                ));
    }

}
