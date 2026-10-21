
package com.ledgerbank.repository;

import com.ledgerbank.model.Account;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Stores and retrieves accounts in memory.
 *
 * This repository keeps data in memory for now.
 * A database-backed implementation can be introduced later.
 */
@Repository
public class AccountRepository {

    // Use the account number as the unique key for each account.
    private final Map<String, Account> accounts = new HashMap<>();

    /**
     * Saves a new account.
     *
     * @param account account to save
     * @throws IllegalArgumentException if an account number already exists
     */
    public void save(Account account) {
        if (accounts.containsKey(account.getAccountNumber())) {
            throw new IllegalArgumentException(
                    "An account with this number already exists.");
        }

        accounts.put(account.getAccountNumber(), account);
    }

    /**
     * Finds an account by its account number.
     *
     * @param accountNumber account number to search for
     * @return an Optional containing the account if found
     */
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accounts.get(accountNumber));
    }

    /**
     * Returns all stored accounts.
     *
     * @return a collection of accounts
     */
    public Collection<Account> findAll() {
        return accounts.values();
    }
}
