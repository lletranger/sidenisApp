package com.keliseev.entity;

import com.keliseev.exception.MyException;
import com.keliseev.to.AccountTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class Vault {

    private static final Map<Long, Account> MAP = new ConcurrentHashMap<>();

    public Vault() {
    }

    public List<Long> getAllAccounts() {
        return MAP.entrySet()
                .stream()
                .filter(e -> e.getValue().isActive())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Account getAccountInfo(Long id) throws MyException {
        if (!MAP.containsKey(id)) {
            throw new MyException("There's no account with id = " + id);
        }

        return MAP.get(id);
    }

    public Account createAccount(AccountTO dto) {
        Account account = new Account(dto.getName(), dto.getAmount());
        MAP.put(account.getId(), account);
        return account;
    }

    public Account closeAccount(Long id) throws MyException {
        if (!MAP.containsKey(id)) {
            throw new MyException("There's no account with id = " + id);
        }

        return MAP.get(id).deactivate();
    }


    public List<Account> transfer(Long from, Long to, Integer amount) throws MyException
    {

        if (!(MAP.containsKey(from) && MAP.containsKey(to))) {
            throw new MyException("One of the accounts doesn't exist: " + from + " or " + to);
        }

        Account donor = MAP.get(from);
        Account recipient = MAP.get(to);

        if(!(donor.isActive() && recipient.isActive())) {
            throw new MyException("Operation can't be finished, account is closed");
        }

        donor.donate(recipient, amount);
        List<Account> result = new ArrayList<>();
        result.add(donor);
        result.add(recipient);
        return List.of(donor, recipient);
    }
}
