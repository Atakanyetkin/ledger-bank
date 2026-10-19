package com.ledgerbank.service;


import com.ledgerbank.model.Account;
import com.ledgerbank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankingServiceTest {

    private AccountRepository accountRepository;
    private BankingService bankingService;

    @BeforeEach
    void setUp() {
        // Create fresh dependencies to isolate each test.
        accountRepository = new AccountRepository();
        bankingService = new BankingService(accountRepository);
    }

    @Test
    void createAccountShouldStoreAccount() {
        // Create an account through the service layer.
        bankingService.createAccount(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );

        Account account = bankingService.getAccount("ACC-1001");

        assertEquals("John Doe", account.getOwnerName());
        assertEquals(
                0,
                new BigDecimal("1000.00").compareTo(account.getBalance())
        );
    }

    @Test
    void depositShouldIncreaseAccountBalance() {
        bankingService.createAccount(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );

        bankingService.deposit("ACC-1001", new BigDecimal("250.00"));

        assertEquals(
                0,
                new BigDecimal("1250.00")
                        .compareTo(bankingService.getAccount("ACC-1001").getBalance())
        );
    }

    @Test
    void withdrawShouldDecreaseAccountBalance() {
        bankingService.createAccount(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );

        bankingService.withdraw("ACC-1001", new BigDecimal("300.00"));

        assertEquals(
                0,
                new BigDecimal("700.00")
                        .compareTo(bankingService.getAccount("ACC-1001").getBalance())
        );
    }

    @Test
    void getAccountShouldFailWhenAccountDoesNotExist() {
        assertThrows(
                IllegalArgumentException.class,
                () -> bankingService.getAccount("ACC-9999")
        );
    }

    @Test
    void createAccountShouldRejectDuplicateAccountNumber() {
        bankingService.createAccount(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> bankingService.createAccount(
                        "ACC-1001",
                        "Jane Doe",
                        new BigDecimal("500.00")
                )
        );
    }
}
