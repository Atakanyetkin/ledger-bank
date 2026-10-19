package com.ledgerbank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        // Create a fresh account before each test to isolate test cases.
        account = new Account(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );
    }

    @Test
    void depositShouldIncreaseBalance() {
        // Deposit 250.00 and verify the resulting balance.
        account.deposit(new BigDecimal("250.00"));

        assertEquals(
                0,
                new BigDecimal("1250.00").compareTo(account.getBalance())
        );
    }

    @Test
    void withdrawShouldDecreaseBalance() {
        // Withdraw 100.00 and verify the resulting balance.
        account.withdraw(new BigDecimal("100.00"));

        assertEquals(
                0,
                new BigDecimal("900.00").compareTo(account.getBalance())
        );
    }

    @Test
    void withdrawShouldFailWhenFundsAreInsufficient() {
        // An account must not spend more money than it contains.
        assertThrows(
                IllegalStateException.class,
                () -> account.withdraw(new BigDecimal("1500.00"))
        );

        // A rejected withdrawal must leave the balance unchanged.
        assertEquals(
                0,
                new BigDecimal("1000.00").compareTo(account.getBalance())
        );
    }

    @Test
    void depositShouldRejectZeroAmount() {
        // Zero is not a valid deposit amount.
        assertThrows(
                IllegalArgumentException.class,
                () -> account.deposit(BigDecimal.ZERO)
        );
    }
}
