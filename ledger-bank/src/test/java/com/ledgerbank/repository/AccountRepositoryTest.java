package com.ledgerbank.repository;

import com.ledgerbank.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountRepositoryTest {

    private AccountRepository repository;
    private Account account;

    @BeforeEach
    void setUp() {
        // Create a fresh repository and account for every test.
        repository = new AccountRepository();
        account = new Account(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );
    }

    @Test
    void saveShouldStoreAccount() {
        // Save an account and verify that it can be retrieved.
        repository.save(account);

        assertTrue(repository.findByAccountNumber("ACC-1001").isPresent());
    }

    @Test
    void findByAccountNumberShouldReturnEmptyWhenAccountDoesNotExist() {
        // Searching for an unknown account should return an empty Optional.
        assertFalse(repository.findByAccountNumber("ACC-9999").isPresent());
    }

    @Test
    void saveShouldRejectDuplicateAccountNumbers() {
        // An account number must uniquely identify one account.
        repository.save(account);

        Account duplicate = new Account(
                "ACC-1001",
                "Jane Doe",
                new BigDecimal("500.00")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(duplicate)
        );
    }

    @Test
    void findAllShouldReturnAllSavedAccounts() {
        // Saving two accounts should produce a collection of two accounts.
        repository.save(account);
        repository.save(new Account(
                "ACC-1002",
                "Jane Doe",
                new BigDecimal("500.00")
        ));

        assertEquals(2, repository.findAll().size());
    }
}
