package com.bank;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Account> accounts;
    private List<Transaction> transactions;

    public Bank() {
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public Account createAccount(String accountHolder, double initialBalance) {
        Account account = new Account(accountHolder, initialBalance);
        accounts.add(account);
        return account;
    }

    public boolean deposit(String accountNumber, double amount) {
        if (amount <= 0) {
            return false;
        }
        
        Account account = findAccount(accountNumber);
        if (account != null) {
            account.deposit(amount);
            transactions.add(new Transaction(accountNumber, "DEPOSIT", amount));
            return true;
        }
        return false;
    }

    public boolean withdraw(String accountNumber, double amount) {
        if (amount <= 0) {
            return false;
        }
        
        Account account = findAccount(accountNumber);
        if (account != null && account.getBalance() >= amount) {
            account.withdraw(amount);
            transactions.add(new Transaction(accountNumber, "WITHDRAWAL", amount));
            return true;
        }
        return false;
    }

    public double getBalance(String accountNumber) {
        Account account = findAccount(accountNumber);
        return account != null ? account.getBalance() : -1;
    }

    public List<Transaction> getTransactionHistory(String accountNumber) {
        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAccountNumber().equals(accountNumber)) {
                accountTransactions.add(transaction);
            }
        }
        return accountTransactions;
    }

    private Account findAccount(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
}
