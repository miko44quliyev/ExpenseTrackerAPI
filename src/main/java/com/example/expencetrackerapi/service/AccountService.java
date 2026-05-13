package com.example.expencetrackerapi.service;

import com.example.expencetrackerapi.dto.request.CreateAccountRequest;
import com.example.expencetrackerapi.dto.request.UpdateAccountRequest;
import com.example.expencetrackerapi.dto.response.AccountResponse;
import com.example.expencetrackerapi.dto.response.AccountResponseSummary;
import com.example.expencetrackerapi.entity.Account;
import com.example.expencetrackerapi.exception.ResourceNotFoundException;
import com.example.expencetrackerapi.mapper.AccountMapper;
import com.example.expencetrackerapi.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public List<AccountResponseSummary> findAll(){
        List<Account> accounts = accountRepository.findAll();
        List<AccountResponseSummary> summary = new ArrayList<>();

        for(Account account: accounts){
            summary.add(accountMapper.toSummary(account));
        }
        return summary;
    }

    public Account findById(Long id){
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with this id: " + id));
    }

    public AccountResponse create(CreateAccountRequest request){
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setFullName(request.getFullName());
        account.setCurrentBalance(request.getCurrentBalance());

        accountRepository.save(account);

        return accountMapper.toResponse(account);
    }

    public AccountResponse update(Long id, UpdateAccountRequest request){
        Account account = findById(id);
        if(request.getEmail() != null){
            account.setEmail(request.getEmail());
        }
        if(request.getFullName() != null){
            account.setFullName(request.getFullName());
        }
        accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    public void delete(Long id){
        Account account = findById(id);
        accountRepository.delete(account);
    }

}
