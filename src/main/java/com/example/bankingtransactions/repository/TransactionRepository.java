package com.example.bankingtransactions.repository;

import com.example.bankingtransactions.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TransactionRepository {

    private final Map<UUID, List<Transaction>> transactionsMap = new HashMap<>();

    public Transaction save(Transaction tm) {
        transactionsMap.computeIfAbsent(tm.getToAccountId(), list -> new ArrayList<>()).add(tm);
        transactionsMap.computeIfAbsent(tm.getFromAccountId(), list -> new ArrayList<>()).add(tm);
        return tm;
    }

    public List<Transaction> findTransactionsByAccountId(UUID id) {
        return transactionsMap.get(id);
    }

    public void deleteAll() {
        transactionsMap.clear();
    }

}
