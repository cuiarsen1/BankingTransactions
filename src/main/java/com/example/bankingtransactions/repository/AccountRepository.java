package com.example.bankingtransactions.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.example.bankingtransactions.model.Account;

@Repository
public class AccountRepository {

    private final Map<UUID, Account> accounts = new HashMap<>();

    public Optional<Account> findById(UUID id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public Map<UUID, Account> findAll() {
        return accounts;
    }

    public Account save(Account account) {
        if (account.getId() == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        accounts.put(account.getId(), account);
        return account;
    }

    public void deleteAll() {
        accounts.clear();
    }
}
