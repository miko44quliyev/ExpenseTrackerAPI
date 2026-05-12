package com.example.expencetrackerapi.repository;

import com.example.expencetrackerapi.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
