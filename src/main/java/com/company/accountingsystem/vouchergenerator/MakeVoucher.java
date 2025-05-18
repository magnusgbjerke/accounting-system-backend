package com.company.accountingsystem.vouchergenerator;

import com.company.accountingsystem.voucher.Voucher;
import com.company.accountingsystem.voucher.dto.VoucherWithPostingsDTO;
import com.company.accountingsystem.voucher.posting.Posting;
import com.company.accountingsystem.year.Year;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MakeVoucher {
    public VoucherWithPostingsDTO makeVoucher(Integer id, Integer year, Integer numberOfPostingSets,
                                              Integer numberOfPostings, String filePath, LocalDateTime createdOn) {
        Voucher voucher = new Voucher(
                id,
                new Year(year),
                null,
                false,
                false,
                filePath,
                null,
                createdOn);
        List<Posting> postingsAccumulated = new ArrayList<>();
        for (int i = 0; i < numberOfPostingSets; i++) {
            List<Posting> postings = new MakePostings().makePostingsSet(voucher, numberOfPostings);
            postings.forEach(posting -> {
                postingsAccumulated.add(posting);
            });

        }
        return new VoucherWithPostingsDTO(voucher, postingsAccumulated);
    }
}
