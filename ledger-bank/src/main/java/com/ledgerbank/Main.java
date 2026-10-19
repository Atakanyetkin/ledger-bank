
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

        // Perform transactions using the account number.
        bankingService.deposit("ACC-1001", new BigDecimal("250.00"));
        bankingService.withdraw("ACC-1001", new BigDecimal("100.00"));

        // Retrieve the account and display its current state.
        var account = bankingService.getAccount("ACC-1001");

        System.out.println("Account number: " + account.getAccountNumber());
        System.out.println("Account owner: " + account.getOwnerName());
        System.out.println("Current balance: " + account.getBalance());
    }
}
