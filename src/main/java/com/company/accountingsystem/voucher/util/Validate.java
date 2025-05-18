package com.company.accountingsystem.voucher.util;

import com.company.accountingsystem.exception.ConflictException;
import com.company.accountingsystem.voucher.dto.VoucherRequestDTO;
import com.company.accountingsystem.voucher.dto.VoucherWithPostingsDTO;
import com.company.accountingsystem.voucher.posting.Posting;
import com.company.accountingsystem.voucher.posting.PostingDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Validate {

    public static void validateVoucher(VoucherRequestDTO voucher) {
        //1. Check if postings are within voucher-year
        voucher.getPostingDTO().forEach(posting -> {
            if (posting.getDate().getYear() != voucher.getYear()) {
                throw new ConflictException("Voucher " + voucher.getId() + " in year " + voucher.getYear() +
                        " has postings outside of " + voucher.getYear() + ".");
            }
        });
        //2. Check if all postings are within the same year
        LocalDate pickFirstDate = voucher.getPostingDTO().getFirst().getDate();
        voucher.getPostingDTO().forEach(posting -> {
            if (posting.getDate().getYear() != pickFirstDate.getYear()) {
                throw new ConflictException("Voucher " + voucher.getId() + " has postings with different years.");
            }
        });
        //3. Check if postings equals 0
        BigDecimal counter = BigDecimal.ZERO;
        for (int x = 0; x < voucher.getPostingDTO().size(); x++) {
            counter = counter.add(voucher.getPostingDTO().get(x).getAmount());
        }
        if (counter.compareTo(BigDecimal.ZERO) != 0) {
            throw new ConflictException("The total amount in voucher " + voucher.getId() +
                    " must be 0, but it is " + counter + ".");
        }
        //4. Group by dates in voucher and check if dates equals 0
        Map<LocalDate, List<PostingDTO>> myObjectsPerId = voucher.getPostingDTO().stream().collect(Collectors.groupingBy(PostingDTO::getDate));
        //4.1 x=sorted id, y=list of sorted id
        myObjectsPerId.forEach((x, y) -> {
            BigDecimal counter2 = BigDecimal.ZERO;
            for (int l = 0; l < y.size(); l++) {
                counter2 = counter2.add(y.get(l).getAmount());
            }
            if (counter2.compareTo(BigDecimal.ZERO) != 0) {
                throw new ConflictException("Postings in voucher " + voucher.getId() +
                        " does equal 0. But some of the postings" + " in voucher " + voucher.getId() + " does not equal 0, based on date");
            }
        });
    }

    public static void validateVoucher(VoucherWithPostingsDTO voucher) {
        //1. Check if postings is empty
        if (voucher.getPostings().isEmpty()) {
            throw new ConflictException("The voucher contains no postings");
        }
        //2. Check if postings are within voucher-year
        voucher.getPostings().forEach(posting -> {
            if (posting.getDate().getYear() != voucher.getVoucher().getYear().getId()) {
                throw new ConflictException("Voucher " + voucher.getVoucher().getYear().getId() +
                        " in year " + voucher.getVoucher().getYear().getId() +
                        "has postings outside of " + voucher.getVoucher().getYear().getId() + ".");
            }
        });
        //3. Check if all postings are within the same year
        LocalDate pickFirstDate = voucher.getPostings().getFirst().getDate();
        voucher.getPostings().forEach(posting -> {
            if (posting.getDate().getYear() != pickFirstDate.getYear()) {
                throw new ConflictException("Voucher " + voucher.getVoucher().getId() + " has postings with different years.");
            }
        });
        //4. Check if postings equals 0
        BigDecimal counter = BigDecimal.ZERO;
        for (int x = 0; x < voucher.getPostings().size(); x++) {
            counter = counter.add(voucher.getPostings().get(x).getAmount());
        }
        if (counter.compareTo(BigDecimal.ZERO) != 0) {
            throw new ConflictException("The total amount in voucher " + voucher.getVoucher().getId() +
                    " must be 0, but it is " + counter + ".");
        }
        //5. Group by dates in voucher and check if dates equals 0
        Map<LocalDate, List<Posting>> myObjectsPerId = voucher.getPostings().stream().collect(Collectors.groupingBy(Posting::getDate));
        //5.1 x=sorted id, y=list of sorted id
        myObjectsPerId.forEach((x, y) -> {
            BigDecimal counter2 = BigDecimal.ZERO;
            for (int l = 0; l < y.size(); l++) {
                counter2 = counter2.add(y.get(l).getAmount());
            }
            if (counter2.compareTo(BigDecimal.ZERO) != 0) {
                throw new ConflictException("Postings in voucher " + voucher.getVoucher().getId() +
                        " does equal 0. But some of the postings" + " in voucher " + voucher.getVoucher().getId() + " does not equal 0, based on date");
            }
        });
    }
}
