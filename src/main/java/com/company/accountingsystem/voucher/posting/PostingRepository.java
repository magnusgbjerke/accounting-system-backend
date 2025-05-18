package com.company.accountingsystem.voucher.posting;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Integer> {

    @Query(value = "SELECT * FROM posting WHERE voucher_id_fk=:id and voucher_year_fk=:year", nativeQuery = true)
    List<Posting> findByVoucherPK(Integer id, Integer year);

    @Query(value = "Select * FROM posting WHERE reconciliation_id=:id", nativeQuery = true)
    Optional<List<Posting>> findListByReconciliation(Integer id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE posting SET reconciliation_id = null WHERE reconciliation_id=:id", nativeQuery = true)
    void updateAllReconciliationIdToNullByReconciliationId(Integer id);
}
