package com.company.accountingsystem.account;

import com.company.accountingsystem.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(Integer id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Account with id " + id + " does not exist."));
    }
}
