package com.rustam.ms_account.dao.repository;

import com.rustam.ms_account.dao.entity.Account;
import com.rustam.ms_account.dao.enums.AccountStatus;
import com.rustam.ms_account.dto.response.AccountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    @Query("SELECT a FROM Account a WHERE a.status = 'ACTIVATED'")
    List<Account> findAllActivatedAccounts();

    @Query("SELECT a FROM Account a WHERE a.status = 'ACTIVATED' AND a.username = :username")
    List<Account> findAllActivatedAccountsAndUsername(@Param("username") String username);

    Account findByUsername(String username);

    Optional<Account> findByIban(String iban);

    Optional<Account> findByUsernameAndIbanAndStatus(String username, String iban, AccountStatus status);
}
