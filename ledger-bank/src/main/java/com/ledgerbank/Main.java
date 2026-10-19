
package com.ledgerbank;

import com.ledgerbank.repository.AccountRepository;
import com.ledgerbank.service.BankingService;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        // Create the repository that stores accounts in memory.
        AccountRepository accountRepository = new AccountRepository();

        // Inject the repository into the banking service.
        BankingService bankingService = new BankingService(accountRepository);

        // Create a new account through the service layer.
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


        var sender = bankingService.getAccount("ACC-1001");
        var recipient = bankingService.getAccount("ACC-1002");

        System.out.println("Before transfer:");
        System.out.println(
                sender.getOwnerName() + ": " + sender.getBalance()
        );
        System.out.println(
                recipient.getOwnerName() + ": " + recipient.getBalance()
        );

// Transfer money from John to Jane.
        bankingService.transfer(
                "ACC-1001",
                "ACC-1002",
                new BigDecimal("200.00")
        );

// Retrieve the updated account balances.
        sender = bankingService.getAccount("ACC-1001");
        recipient = bankingService.getAccount("ACC-1002");

        System.out.println("\nAfter transfer:");
        System.out.println(
                sender.getOwnerName() + ": " + sender.getBalance()
        );
        System.out.println(
                recipient.getOwnerName() + ": " + recipient.getBalance()
        );
    }
}
