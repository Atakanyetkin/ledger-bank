package com.ledgerbank.service;


import com.ledgerbank.model.Account;
import com.ledgerbank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ledgerbank.exception.AccountNotFoundException;

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
                AccountNotFoundException.class,
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

    @Test
    void transferShouldUpdateBothAccountBalances() {
        // Create sender and recipient accounts with known balances.
        bankingService.createAccount(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );
        bankingService.createAccount(
                "ACC-1002",
                "Jane Doe",
                new BigDecimal("500.00")
        );

        // Transfer 200.00 from the sender to the recipient.
        bankingService.transfer(
                "ACC-1001",
                "ACC-1002",
                new BigDecimal("200.00")
        );

        assertEquals(
                0,
                new BigDecimal("800.00")
                        .compareTo(bankingService.getAccount("ACC-1001").getBalance())
        );
        assertEquals(
                0,
                new BigDecimal("700.00")
                        .compareTo(bankingService.getAccount("ACC-1002").getBalance())
        );
    }

    @Test
    void transferShouldFailWhenSenderHasInsufficientFunds() {
        bankingService.createAccount(
                "ACC-1001",
                "John Doe",
                new BigDecimal("100.00")
        );
        bankingService.createAccount(
                "ACC-1002",
                "Jane Doe",
                new BigDecimal("500.00")
        );

        assertThrows(
                IllegalStateException.class,
                () -> bankingService.transfer(
                        "ACC-1001",
                        "ACC-1002",
                        new BigDecimal("200.00")
                )
        );

        // A rejected transfer must leave both balances unchanged.
        assertEquals(
                0,
                new BigDecimal("100.00")
                        .compareTo(bankingService.getAccount("ACC-1001").getBalance())
        );
        assertEquals(
                0,
                new BigDecimal("500.00")
                        .compareTo(bankingService.getAccount("ACC-1002").getBalance())
        );
    }

    @Test
    void transferShouldRejectSameAccount() {
        bankingService.createAccount(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> bankingService.transfer(
                        "ACC-1001",
                        "ACC-1001",
                        new BigDecimal("100.00")
                )

        );
        assertEquals(
                0,
                new BigDecimal("1000.00")
                        .compareTo(
                                bankingService.getAccount("ACC-1001").getBalance()
                        )
        );
    }

    @Test
    void transferShouldFailWhenRecipientDoesNotExist() {
        bankingService.createAccount(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );

        assertThrows(
                AccountNotFoundException.class,
                () -> bankingService.transfer(
                        "ACC-1001",
                        "ACC-9999",
                        new BigDecimal("100.00")
                )
        );

        // A missing recipient must not cause money to leave the sender.
        assertEquals(
                0,
                new BigDecimal("1000.00")
                        .compareTo(bankingService.getAccount("ACC-1001").getBalance())
        );
    }

    @Test
    void transferShouldRejectZeroAmount() {
        bankingService.createAccount(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );
        bankingService.createAccount(
                "ACC-1002",
                "Jane Doe",
                new BigDecimal("500.00")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> bankingService.transfer(
                        "ACC-1001",
                        "ACC-1002",
                        BigDecimal.ZERO
                )
        );

        // An invalid transfer must leave both balances unchanged.
        assertEquals(
                0,
                new BigDecimal("1000.00")
                        .compareTo(bankingService.getAccount("ACC-1001").getBalance())
        );
        assertEquals(
                0,
                new BigDecimal("500.00")
                        .compareTo(bankingService.getAccount("ACC-1002").getBalance())
        );
    }

    @Test
    void transferShouldRejectNegativeAmount() {
        bankingService.createAccount(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );
        bankingService.createAccount(
                "ACC-1002",
                "Jane Doe",
                new BigDecimal("500.00")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> bankingService.transfer(
                        "ACC-1001",
                        "ACC-1002",
                        new BigDecimal("-100.00")
                )
        );

        // A rejected transfer must leave both balances unchanged.
        assertEquals(
                0,
                new BigDecimal("1000.00")
                        .compareTo(bankingService.getAccount("ACC-1001").getBalance())
        );
        assertEquals(
                0,
                new BigDecimal("500.00")
                        .compareTo(bankingService.getAccount("ACC-1002").getBalance())
        );
    }

    @Test
    void transferShouldRejectNullSenderAccountNumber() {
        assertThrows(
                NullPointerException.class,
                () -> bankingService.transfer(
                        null,
                        "ACC-1002",
                        new BigDecimal("100.00")
                )
        );
    }

    @Test
    void transferShouldRejectNullRecipientAccountNumber() {
        assertThrows(
                NullPointerException.class,
                () -> bankingService.transfer(
                        "ACC-1001",
                        null,
                        new BigDecimal("100.00")
                )
        );
    }

    @Test
    void constructorShouldRejectNullRepository() {
        assertThrows(
                NullPointerException.class,
                () -> new BankingService(null)
        );
    }


    @Test
    void transferShouldRejectBlankSenderAccountNumber() {
        assertThrows(
                IllegalArgumentException.class,
                () -> bankingService.transfer(
                        "",
                        "ACC-1002",
                        new BigDecimal("100.00")
                )
        );
    }

    @Test
    void transferShouldRejectBlankRecipientAccountNumber() {
        assertThrows(
                IllegalArgumentException.class,
                () -> bankingService.transfer(
                        "ACC-1001",
                        "   ",
                        new BigDecimal("100.00")
                )
        );
    }

    @Test
    void transferShouldRejectNullAmount() {
        bankingService.createAccount(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );
        bankingService.createAccount(
                "ACC-1002",
                "Jane Doe",
                new BigDecimal("500.00")
        );

        assertThrows(
                NullPointerException.class,
                () -> bankingService.transfer(
                        "ACC-1001",
                        "ACC-1002",
                        null
                )
        );

        assertEquals(
                0,
                new BigDecimal("1000.00")
                        .compareTo(bankingService.getAccount("ACC-1001").getBalance())
        );
        assertEquals(
                0,
                new BigDecimal("500.00")
                        .compareTo(bankingService.getAccount("ACC-1002").getBalance())
        );
    }


    @Test
    void transferShouldFailWhenSourceAndDestinationAreTheSame() {
        Account account = new Account(
                "ACC-1001",
                "Atakan",
                new BigDecimal("1000.00")
        );
        accountRepository.save(account);

        assertThrows(
                IllegalArgumentException.class,
                () -> bankingService.transfer(
                        "ACC-1001",
                        "ACC-1001",
                        new BigDecimal("100.00")
                )
        );

        assertEquals(
                new BigDecimal("1000.00"),
                account.getBalance()
        );
    }

}
