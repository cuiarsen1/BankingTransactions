package com.example.bankingtransactions.service;

import com.example.bankingtransactions.dto.*;
import com.example.bankingtransactions.enums.TransactionType;
import com.example.bankingtransactions.exception.AccountNotFoundException;
import com.example.bankingtransactions.exception.InsufficientFundsException;
import com.example.bankingtransactions.exception.TransferException;
import com.example.bankingtransactions.model.Account;
import com.example.bankingtransactions.model.Transaction;
import com.example.bankingtransactions.repository.AccountRepository;
import com.example.bankingtransactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountDto createAccount(CreateAccountRequest request) {
        if (request.getInitialBalance().scale() > 2) {
            throw new TransferException("Input amount cannot have more than 2 decimal places");
        }

        log.info("Creating account with initial balance: {}", roundMoney(request.getInitialBalance()));
        Account account = new Account();
        account.setName(request.getName());
        account.setBalance(roundMoney(request.getInitialBalance()));
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {}", savedAccount.getId());
        return mapToAccountDto(savedAccount);
    }

    public AccountDto getAccount(UUID accountId) {
        log.info("Retrieving account with ID: {}", accountId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        log.info("Account found: {}", accountId);
        return mapToAccountDto(account);
    }

    public TransactionDto transferFunds(TransferRequest transferRequest) {
        UUID fromAccountId = transferRequest.getFromAccountId();
        UUID toAccountId = transferRequest.getToAccountId();
        BigDecimal amount = transferRequest.getAmount();

        if (amount.scale() > 2) {
            throw new TransferException("Input amount cannot have more than 2 decimal places");
        }

        log.info("Processing transfer of ${} from account {} to account {}", roundMoney(amount), fromAccountId, toAccountId);

        if (fromAccountId.equals(toAccountId)) {
            throw new TransferException("Transfer failed: Source and destination accounts are the same: " + fromAccountId);
        }

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));

        if (!fromAccount.withdraw(amount)) {
            throw new InsufficientFundsException(fromAccountId, amount);
        }

        try {
            toAccount.deposit(amount);
        } catch (Exception e) {
            // If the deposit fails, roll back the withdrawal
            log.error("Deposit to account {} failed unexpectedly. Rolling back withdrawal from account {}",
                    toAccountId, fromAccountId, e);
            fromAccount.deposit(amount);
            throw new TransferException("Failed to deposit funds into account " + toAccountId + ". Transfer cancelled.");
        }

        // Create transaction record after successful balance updates
        Transaction transaction = new Transaction();
        transaction.setFromAccountId(fromAccountId);
        transaction.setToAccountId(toAccountId);
        transaction.setAmount(roundMoney(amount));
        transaction.setType(TransactionType.TRANSFER);
        transaction.setDescription(transferRequest.getDescription());

        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("Transfer successful. Transaction ID: {}. New account balances: Account {}: {}, Account {}: {}",
                savedTransaction.getId(),
                fromAccountId,
                roundMoney(fromAccount.getBalance()),
                toAccountId,
                roundMoney(toAccount.getBalance())
        );

        return mapToTransactionDto(savedTransaction);
    }

    public List<TransactionDto> getTransactionHistory(UUID accountId) {
        log.info("Retrieving transaction history for account with ID: {}", accountId);

        // Check account exists
        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        List<Transaction> transactions = transactionRepository.findTransactionsByAccountId(accountId);
        if (transactions == null) {
            transactions = List.of();
        }
        log.info("Found {} transactions for account with ID: {}", transactions.size(), accountId);
        return transactions.stream()
                .map(this::mapToTransactionDto)
                .toList();
    }

    public TransactionDto depositFunds(DepositRequest depositRequest) {
        UUID accountId = depositRequest.getToAccountId();
        BigDecimal amount = depositRequest.getAmount();

        if (amount.scale() > 2) {
            throw new TransferException("Input amount cannot have more than 2 decimal places");
        }

        log.info("Processing deposit of ${} into account {}", roundMoney(amount), accountId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        try {
            account.deposit(amount);
        } catch (Exception e) {
            throw new TransferException("Failed to deposit funds into account " + accountId);
        }

        // Create transaction record after successful balance update
        Transaction transaction = new Transaction();
        transaction.setToAccountId(accountId);
        transaction.setAmount(roundMoney(amount));
        transaction.setType(TransactionType.DEPOSIT);

        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("Deposit successful. Transaction ID: {}. New balance for account {}: {}",
                savedTransaction.getId(), accountId, roundMoney(account.getBalance()));

        return mapToTransactionDto(savedTransaction);
    }

    public TransactionDto withdrawFunds(WithdrawRequest withdrawRequest) {
        UUID accountId = withdrawRequest.getFromAccountId();
        BigDecimal amount = withdrawRequest.getAmount();

        if (amount.scale() > 2) {
            throw new TransferException("Input amount cannot have more than 2 decimal places");
        }

        log.info("Processing withdrawal of ${} from account {}", roundMoney(amount), accountId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        if (!account.withdraw(amount)) {
            throw new InsufficientFundsException(accountId, amount);
        }

        // Create transaction record after successful balance update
        Transaction transaction = new Transaction();
        transaction.setFromAccountId(accountId);
        transaction.setAmount(roundMoney(amount));
        transaction.setType(TransactionType.WITHDRAWAL);

        Transaction savedTransaction = transactionRepository.save(transaction);

        log.info("Withdrawal successful. Transaction ID: {}. New balance for account {}: {}",
                savedTransaction.getId(), accountId, roundMoney(account.getBalance()));

        return mapToTransactionDto(savedTransaction);
    }

    private AccountDto mapToAccountDto(Account account) {
        return new AccountDto(account.getId(), account.getName(), "$" + account.getBalance(), account.getCreatedAt());
    }

    private TransactionDto mapToTransactionDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getFromAccountId(),
                transaction.getToAccountId(),
                "$" + transaction.getAmount(),
                transaction.getType(),
                transaction.getDescription(),
                transaction.getTimestamp()
        );
    }

    public static BigDecimal roundMoney(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(2, RoundingMode.HALF_EVEN);
    }

}
