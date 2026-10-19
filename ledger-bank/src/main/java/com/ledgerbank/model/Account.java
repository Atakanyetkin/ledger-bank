
package com.ledgerbank.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a bank account and protects its balance from invalid changes.
 *
 * BigDecimal is used for money to avoid floating-point precision errors.
 */
public class Account {

    private final String accountNumber;
    private final String ownerName;
    private BigDecimal balance;

    /**
     * Creates a bank account with an initial balance.
     *
     * @param accountNumber unique identifier for the account
     * @param ownerName name of the account owner
     * @param initialBalance starting balance; cannot be negative
     */
    public Account(String accountNumber, String ownerName,
                   BigDecimal initialBalance) {
        this.accountNumber = requireText(accountNumber, "Account number");
        this.ownerName = requireText(ownerName, "Owner name");
        Objects.requireNonNull(initialBalance, "Initial balance cannot be null");

        if (initialBalance.signum() < 0) {
            throw new IllegalArgumentException(
                    "Initial balance cannot be negative.");
        }

        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Adds money to the account.
     *
     * @param amount amount to deposit; must be positive
     */
    public void deposit(BigDecimal amount) {
        validatePositiveAmount(amount);
        balance = balance.add(amount);
    }

    /**
     * Withdraws money if the account has sufficient funds.
     *
     * @param amount amount to withdraw; must be positive
     */
    public void withdraw(BigDecimal amount) {
        validatePositiveAmount(amount);

        if (balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds.");
        }

        balance = balance.subtract(amount);
    }

    private static void validatePositiveAmount(BigDecimal amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero.");
        }
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");

        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be blank.");
        }

        return value;
    }
}
