package com.bank;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create a new bank
        Bank bank = new Bank();
        
        // Create some accounts
        Account account1 = bank.createAccount("John Doe", 1000.0);
        Account account2 = bank.createAccount("Jane Smith", 500.0);
        
        // Perform some transactions
        System.out.println("Initial balance of " + account1.getAccountHolder() + ": " + 
                         bank.getBalance(account1.getAccountNumber()));
        
        // Deposit money
        bank.deposit(account1.getAccountNumber(), 500.0);
        System.out.println("After deposit of 500.0: " + 
                         bank.getBalance(account1.getAccountNumber()));
        
        // Withdraw money
        bank.withdraw(account1.getAccountNumber(), 200.0);
        System.out.println("After withdrawal of 200.0: " + 
                         bank.getBalance(account1.getAccountNumber()));
        
        // Show transaction history
        System.out.println("\nTransaction History for " + account1.getAccountHolder() + ":");
        List<Transaction> transactions = bank.getTransactionHistory(account1.getAccountNumber());
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getType() + " of " + 
                             transaction.getAmount() + " at " + 
                             transaction.getTimestamp());
        }
    }
} 