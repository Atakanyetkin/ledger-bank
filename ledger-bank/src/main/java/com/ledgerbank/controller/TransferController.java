
package com.ledgerbank.controller;

import com.ledgerbank.dto.TransferRequest;
import com.ledgerbank.model.Account;
import com.ledgerbank.service.BankingService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final BankingService bankingService;

    public TransferController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @PostMapping
    public Account transfer(@Valid @RequestBody TransferRequest request) {
        bankingService.transfer(
                request.fromAccountNumber(),
                request.toAccountNumber(),
                request.amount()
        );

        return bankingService.getAccount(request.fromAccountNumber());
    }
}
