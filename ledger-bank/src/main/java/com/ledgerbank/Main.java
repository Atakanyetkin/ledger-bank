
package com.ledgerbank;

import com.ledgerbank.model.Account;
import com.ledgerbank.service.BankingService;
import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        // Create a bank account with an initial balance of 1000.00.
        Account account = new Account(
                "ACC-1001",
                "John Doe",
                new BigDecimal("1000.00")
        );

        // Create the service responsible for banking operations.
        BankingService bankingService = new BankingService();

        // Perform banking operations through the service layer.
        bankingService.deposit(account, new BigDecimal("250.00"));
        bankingService.withdraw(account, new BigDecimal("100.00"));

        // Display the account details and the final balance.
        System.out.println("Account number: " + account.getAccountNumber());
        System.out.println("Account owner: " + account.getOwnerName());
        System.out.println("Current balance: " + account.getBalance());
    }
}
