package com.keliseev.entity;

import com.keliseev.exception.MyException;

import java.util.concurrent.atomic.AtomicLong;

public class Account {

    private static AtomicLong lastId = new AtomicLong(0L);
    private static final Object LOCK_1 = new Object();
    private static final Object LOCK_2 = new Object();

    private final long id;
    private final String name;
    private int balance;
    private boolean isActive;

    Account(String name, int balance) {
        this.id = lastId.incrementAndGet();
        this.name = name;
        this.balance = balance;
        this.isActive = true;
    }

    public String getName() {
        return name;
    }

    public synchronized int getBalance() {
        return balance;
    }

    public long getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    Account deactivate() {
        isActive = false;
        return this;
    }

    void donate(Account recipient, int amount) throws MyException {
        synchronized (LOCK_1) {
            synchronized (LOCK_2) {
                if (this.balance >= amount) {
                    this.balance -= amount;
                    recipient.balance += amount;
                } else {
                    throw new MyException("Insufficient funds!");
                }
            }
        }
    }
}
