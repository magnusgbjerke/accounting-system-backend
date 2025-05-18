package com.company.accountingsystem.report;

import com.company.accountingsystem.voucher.Voucher;
import com.company.accountingsystem.voucher.VoucherRepository;
import com.company.accountingsystem.voucher.dto.VoucherWithPostingsDTO;
import com.company.accountingsystem.voucher.service.HelperService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportService {
    private final HelperService helperService;
    private final VoucherRepository voucherRepository;

    public ReportService(VoucherRepository voucherRepository,
                         HelperService helperService) {
        this.voucherRepository = voucherRepository;
        this.helperService = helperService;
    }

    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        List<TransactionDTO> response = new ArrayList<>();
        // Get all vouchers
        List<Voucher> allVouchers = voucherRepository.findAllByPostedOrNotPosted(true);
        // Get transactions for each voucher
        allVouchers.forEach(voucher -> {
            List<TransactionDTO> response2 = new ArrayList<>();
            VoucherWithPostingsDTO voucher2 = helperService.getVoucherWithPostings(voucher.getId(), voucher.getYear().getId());
            voucher2.getPostings().forEach(posting -> {
                response2.add(new TransactionDTO(posting.getId(), posting.getVoucher().getId(), posting.getDate(), posting.getAccount().getId(), posting.getAccount().getName(), posting.getAmount()));
            });
            response2.forEach(transaction -> {
                response.add(transaction);
            });
        });
        // Sort by date
        response.sort(Comparator.comparing(TransactionDTO::getDate));
        // Sort by id
        response.sort(Comparator.comparing(TransactionDTO::getVoucherId));
        // Sort by year
        response.sort(
                Comparator.comparing((TransactionDTO transaction) -> transaction.getDate().getYear())
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

