package com.company.accountingsystem.voucher.tempvoucher;

import com.company.accountingsystem.account.Account;
import com.company.accountingsystem.account.AccountService;
import com.company.accountingsystem.exception.ConflictException;
import com.company.accountingsystem.voucher.Voucher;
import com.company.accountingsystem.voucher.VoucherRepository;
import com.company.accountingsystem.voucher.dto.*;
import com.company.accountingsystem.voucher.posting.Posting;
import com.company.accountingsystem.voucher.posting.PostingRepository;
import com.company.accountingsystem.voucher.service.HelperService;
import com.company.accountingsystem.voucher.util.Converter;
import com.company.accountingsystem.voucher.util.Document;
import com.company.accountingsystem.voucher.util.Validate;
import com.company.accountingsystem.year.Year;
import com.company.accountingsystem.year.YearService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class TempVoucherService {
    private final YearService yearService;
    private final HelperService helperService;
    private final AccountService accountService;
    private final VoucherRepository voucherRepository;
    private final PostingRepository postingRepository;

    public TempVoucherService(HelperService helperService,
                              AccountService accountService,
                              VoucherRepository voucherRepository,
                              PostingRepository postingRepository,
                              YearService yearService) {
        this.helperService = helperService;
        this.accountService = accountService;
        this.voucherRepository = voucherRepository;
        this.postingRepository = postingRepository;
        this.yearService = yearService;
    }

    public ResponseEntity<List<TempVoucherDTO>> getTempVouchers() {
        List<TempVoucherDTO> response = new ArrayList<>();
        // Get all vouchers by not posted
        List<Voucher> allVouchers = voucherRepository.findAllByPostedOrNotPosted(false);
        // Get posting for each voucher
        allVouchers.forEach(voucher -> {
            VoucherWithPostingsDTO vwp = helperService.getVoucherWithPostings(voucher.getId(), voucher.getYear().getId());
            response.add(Converter.convertToTempVoucher(vwp));
        });
        // Sort response by id
        response.sort(Comparator.comparing(TempVoucherDTO::getTempId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<VoucherResponseDTO> postTempVoucher(Integer tempId, Integer tempYear) {
        // Check if temp exist
        Voucher voucher = helperService.getVoucher(tempId, tempYear);
        // Check if already posted
        if (voucher.getCreatedOn() != null) {
            throw new ConflictException("Voucher or TempVoucher with id " + tempId + " and year " + tempYear + " is already posted");
        }
        // Convert to VoucherWithPostingsDTO
        VoucherWithPostingsDTO voucher2 = helperService.getVoucherWithPostings(tempId, tempYear);
        // Validate voucher
        Validate.validateVoucher(voucher2);
        // Post voucher(If createdOn is set then the voucher is considered posted)
        voucher.setCreatedOn(LocalDateTime.now());
        // Make response
        VoucherResponseDTO response = Converter.convertToVoucherResponse(voucher2, false);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<TempVoucherDTO> createTempVoucher(TempVoucherDTO voucher) {
        // Check if id already-exist and if year exist
        helperService.checkIfVoucherExist(voucher.getTempId(), voucher.getTempYear());
        if (voucher.getTempYear() != null) {
            yearService.getYear(voucher.getTempYear());
        }
        // Save document
        String filePath = Document.saveDocumentIfAttached(voucher);
        Voucher newVoucher = new Voucher(
                (voucher.getTempId() == null) ? voucherRepository.findLastAvailableVoucherIds(1, ((voucher.getTempYear() == null) ?
                        LocalDateTime.now().getYear() : voucher.getTempYear())).getFirst() : voucher.getTempId(),
                (voucher.getTempYear() == null) ? new Year(LocalDateTime.now().getYear()) : new Year(voucher.getTempYear()),
                null,
                false,
                false,
                filePath,
                LocalDateTime.now(),
                null);
        // Save voucher
        voucherRepository.save(newVoucher);
        // Make response
        TempVoucherDTO response = new TempVoucherDTO(
                newVoucher.getId(),
                newVoucher.getYear().getId(),
                (voucher.getDocumentDTO() != null) ? new DocumentDTO(voucher.getDocumentDTO().getFileData(), voucher.getDocumentDTO().getFileName()) : null,
                voucher.getPostingDTO(),
                newVoucher.getTempCreatedOn()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    public void updateTempVoucher(TempVoucherDTO voucher) {
        // Check if temp exist
        Voucher voucher2 = helperService.getVoucher(voucher.getTempId(), voucher.getTempYear());
        // Check if voucher is a TempVoucher
        if (voucher2.getCreatedOn() != null) {
            throw new ConflictException("It is only possible to update TempVouchers");
        }
        // Delete old postings
        List<Posting> postings = postingRepository.findByVoucherPK(voucher.getTempId(), voucher.getTempYear());
        postings.forEach(posting -> {
            postingRepository.delete(posting);
        });
        // Save new postings
        voucher.getPostingDTO().forEach(posting -> {
            // Check if account exist
            Account account = accountService.getAccount(posting.getAccountId());
            postingRepository.save(
                    new Posting(
                            voucher2,
                            posting.getDate(),
                            account,
                            posting.getAmount()
                    ));
        });
        // Final steps in the update process, as it is not handled transactionally.
        // Delete the old document.
        Document.deleteDocumentIfAttached(voucher2);
        // Save new document
        String filePath = Document.saveDocumentIfAttached(voucher);
        // Update voucher filepath
        voucher2.setFilePath(filePath);
    }

    @Transactional
    public void deleteTempVoucher(Integer tempId, Integer tempYear) {
        // Check if temp exist
        Voucher voucher = helperService.getVoucher(tempId, tempYear);
        // Check if voucher is a TempVoucher
        if (voucher.getCreatedOn() != null) {
            throw new ConflictException("It is only possible to delete TempVouchers");
        }
        // Delete postings
        List<Posting> postings = postingRepository.findByVoucherPK(voucher.getId(), voucher.getYear().getId());
        postings.forEach(posting -> {
            postingRepository.delete(posting);
        });
        // Delete voucher
        voucherRepository.delete(voucher);
    }
}



