package com.company.accountingsystem.voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, VoucherPK> {
    @Override
    default List<Voucher> findAll() {
        throw new UnsupportedOperationException("findAll() is not supported for this repository.");
    }

    @Query(value = "SELECT * FROM voucher WHERE id=:id and year_id=:year", nativeQuery = true)
    Optional<Voucher> findByVoucherPK(Integer id, Integer year);

    @Query(value = "    SELECT g.num AS next_available\n" +
            "    FROM generate_series(1, (SELECT COALESCE(MAX(id), 0) + 100 FROM voucher WHERE year_id = :year)) AS g(num)\n" +
            "    LEFT JOIN voucher v \n" +
            "    ON g.num = v.id AND v.year_id = :year\n" +
            "    WHERE v.id IS NULL\n" +
            "    ORDER BY g.num\n" +
            "    LIMIT :limit", nativeQuery = true)
    List<Integer> findLastAvailableVoucherIds(Integer limit, Integer year);

    @Query(value = "SELECT * FROM voucher WHERE voucher_id_fk=:id and voucher_year_fk=:year", nativeQuery = true)
    Optional<Voucher> findUpdatedVoucherIdByVoucherPK(Integer id, Integer year);

    @Query(value = "SELECT * FROM voucher WHERE \n" +
            "            (:posted = true AND created_on IS NOT NULL) OR\n" +
            "            (:posted = false AND created_on IS NULL)", nativeQuery = true)
    List<Voucher> findAllByPostedOrNotPosted(Boolean posted);
}

