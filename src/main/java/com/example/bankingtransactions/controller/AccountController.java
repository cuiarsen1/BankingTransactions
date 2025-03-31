package com.example.bankingtransactions.controller;

import com.example.bankingtransactions.dto.*;
import com.example.bankingtransactions.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // POST endpoint to create a new bank account
    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        AccountDto account = accountService.createAccount(createAccountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable UUID accountId) {
        AccountDto account = accountService.getAccount(accountId);
        return ResponseEntity.ok(account);
    }

    // POST endpoint to transfer funds between accounts
    @PostMapping("/transfer")
    public ResponseEntity<TransactionDto> transferFunds(@Valid @RequestBody TransferRequest transferRequest) {
        TransactionDto transactionDto = accountService.transferFunds(transferRequest);
        return ResponseEntity.ok(transactionDto);
    }

    // GET endpoint to retrieve transaction history for a specific account
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactionHistory(@PathVariable UUID accountId) {
        List<TransactionDto> transactions = accountService.getTransactionHistory(accountId);
        return ResponseEntity.ok(transactions);
    }

    // POST endpoint to deposit funds into an account
    @PostMapping("/deposit")
    public ResponseEntity<TransactionDto> depositFunds(@Valid @RequestBody DepositRequest request) {
        TransactionDto transactionDto = accountService.depositFunds(request);
        return ResponseEntity.ok(transactionDto);
    }

    // POST endpoint to withdraw funds from an account
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDto> withdrawFunds(@Valid @RequestBody WithdrawRequest request) {
        TransactionDto transactionDto = accountService.withdrawFunds(request);
        return ResponseEntity.ok(transactionDto);
    }
}
