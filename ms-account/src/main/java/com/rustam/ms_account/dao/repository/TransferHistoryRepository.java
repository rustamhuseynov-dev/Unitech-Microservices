package com.rustam.ms_account.dao.repository;

import com.rustam.ms_account.dao.entity.TransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferHistoryRepository extends JpaRepository<TransferHistory,Long> {
}
