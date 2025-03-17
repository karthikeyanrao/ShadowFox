package com.bank;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankTest {
    private Bank bank;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        testAccount = bank.createAccount("John Doe", 1000.0);
    }

    @Test
    void testCreateAccount() {
        Account account = bank.createAccount("Jane Doe", 500.0);
        assertNotNull(account);
        assertEquals("Jane Doe", account.getAccountHolder());
        assertEquals(500.0, account.getBalance());
    }

    @Test
    void testDeposit() {
        assertTrue(bank.deposit(testAccount.getAccountNumber(), 500.0));
        assertEquals(1500.0, bank.getBalance(testAccount.getAccountNumber()));
        
        // Test invalid deposit
        assertFalse(bank.deposit(testAccount.getAccountNumber(), -100));
        assertFalse(bank.deposit("INVALID_ACC", 100));
    }

    @Test
    void testWithdraw() {
        assertTrue(bank.withdraw(testAccount.getAccountNumber(), 500.0));
        assertEquals(500.0, bank.getBalance(testAccount.getAccountNumber()));
        
        // Test invalid withdrawal
        assertFalse(bank.withdraw(testAccount.getAccountNumber(), 2000.0));
        assertFalse(bank.withdraw(testAccount.getAccountNumber(), -100));
        assertFalse(bank.withdraw("INVALID_ACC", 100));
    }

    @Test
    void testGetBalance() {
        assertEquals(1000.0, bank.getBalance(testAccount.getAccountNumber()));
        assertEquals(-1, bank.getBalance("INVALID_ACC"));
    }

    @Test
    void testTransactionHistory() {
        bank.deposit(testAccount.getAccountNumber(), 500.0);
        bank.withdraw(testAccount.getAccountNumber(), 200.0);
        
        List<Transaction> transactions = bank.getTransactionHistory(testAccount.getAccountNumber());
        assertEquals(2, transactions.size());
        
        // Verify transaction details
        assertEquals("DEPOSIT", transactions.get(0).getType());
        assertEquals(500.0, transactions.get(0).getAmount());
        assertEquals("WITHDRAWAL", transactions.get(1).getType());
        assertEquals(200.0, transactions.get(1).getAmount());
    }
} 