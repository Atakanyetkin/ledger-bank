package com.ledgerbank.controller;

import com.ledgerbank.model.Account;
import com.ledgerbank.service.BankingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerbank.dto.CreateAccountRequest;
import com.ledgerbank.dto.MoneyRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final BankingService bankingService;

    public  AccountController(BankingService bankingService) {
        this.bankingService = bankingService;

    }

    @PostMapping
    public void createAccount(@RequestBody CreateAccountRequest request) {
        bankingService.createAccount(
                request.accountNumber(),
                request.ownerName(),
                request.initialBalance()
        );
    }

    @GetMapping("/{accountNumber}")
    public Account getAccount(@PathVariable String accountNumber) {
        return bankingService.getAccount(accountNumber);
    }

    @PostMapping("/{accountNumber}/deposit")
    public Account deposit(
            @PathVariable String accountNumber,
            @RequestBody MoneyRequest request) {

        bankingService.deposit(accountNumber, request.amount());
        return bankingService.getAccount(accountNumber);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public Account withdraw(
            @PathVariable String accountNumber,
            @RequestBody MoneyRequest request) {

        bankingService.withdraw(accountNumber, request.amount());
        return bankingService.getAccount(accountNumber);
    }
}
