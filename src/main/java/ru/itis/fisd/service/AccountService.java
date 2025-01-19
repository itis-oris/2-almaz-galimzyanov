package ru.itis.fisd.service;

import ru.itis.fisd.mapper.impl.AccountMapper;
import ru.itis.fisd.model.AccountEntity;
import ru.itis.fisd.repository.AccountRepository;

import java.util.Optional;

public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService() {
        this.accountRepository = new AccountRepository(new AccountMapper());
    }

    public Optional<AccountEntity> getById(Long id) {
        return accountRepository.getById(id);
    }

    public boolean save(AccountEntity accountEntity) {
        return accountRepository.insert(accountEntity);
    }

    public boolean update(AccountEntity accountEntity) {
        return accountRepository.update(accountEntity);
    }

    public boolean updateInfo(AccountEntity accountEntity) {
        return accountRepository.updateInfo(accountEntity);
    }

}
