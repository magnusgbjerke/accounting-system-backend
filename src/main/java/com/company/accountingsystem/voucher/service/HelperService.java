package com.company.accountingsystem.voucher.service;

import com.company.accountingsystem.account.Account;
import com.company.accountingsystem.account.AccountService;
import com.company.accountingsystem.exception.ConflictException;
import com.company.accountingsystem.exception.EntityNotFoundException;
import com.company.accountingsystem.voucher.Voucher;
import com.company.accountingsystem.voucher.VoucherRepository;
import com.company.accountingsystem.voucher.dto.*;
import com.company.accountingsystem.voucher.posting.Posting;
import com.company.accountingsystem.voucher.posting.PostingRepository;
import com.company.accountingsystem.voucher.util.Converter;
import com.company.accountingsystem.voucher.util.Document;
import com.company.accountingsystem.year.Year;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HelperService {

    private final VoucherRepository voucherRepository;
    private final PostingRepository postingRepository;
    private final AccountService accountService;

    public HelperService(VoucherRepository voucherRepository, PostingRepository postingRepository,
                         AccountService accountService) {
        this.voucherRepository = voucherRepository;
        this.postingRepository = postingRepository;
        this.accountService = accountService;
    }

    public Voucher reverseVoucher(Integer id, Integer year) {
        Voucher voucher = getVoucher(id, year);
        // Create and save reverse voucher
        Voucher reverseVoucher = new Voucher(
                voucherRepository.findLastAvailableVoucherIds(1, year).getFirst(), // New id based on next available voucher id
                voucher.getYear(),
                voucher,
                false,
                true, // Mark as reversed-voucher
                voucher.getFilePath(),
                null,
                LocalDateTime.now()
        );
        voucherRepository.save(reverseVoucher);
        // Get postings based on voucherPK
        List<Posting> postings = postingRepository.findByVoucherPK(id, year);
        // Create and save reverse postings
        postings.forEach(posting -> {
            Posting reversePosting = new Posting(
                    reverseVoucher,
                    posting.getDate(),
                    posting.getAccount(),
                    posting.getAmount().negate() // Reverse amount
            );
            postingRepository.save(reversePosting);
        });
        return reverseVoucher;
    }

    public List<Voucher> getUpdatedVouchers(Voucher voucher) {
        List<Voucher> updatedVouchers = new ArrayList<>();
        // Add downstream
        Voucher currentDownstream = voucher;
        while (currentDownstream != null) {
            // Check if id exist
            Voucher voucher2 = getVoucher(currentDownstream.getId(), currentDownstream.getYear().getId());
            updatedVouchers.add(voucher2);
            currentDownstream = currentDownstream.getUpdatedVoucher();
        }
        // Add upstream
        Voucher currentUpstream = voucher;
        while (currentUpstream != null) {
            // Check if id exist
            Voucher finalCurrentUpstream = currentUpstream;
            Voucher voucher2 = getVoucher(finalCurrentUpstream.getId(), finalCurrentUpstream.getYear().getId());
            if (!currentUpstream.equals(voucher)) { // Exclude first voucher(already added downstream)
                updatedVouchers.add(voucher2);
            }
            // Check if voucher is updated before
            Optional<Voucher> optionalVoucher = voucherRepository.findUpdatedVoucherIdByVoucherPK(
                    currentUpstream.getId(), currentUpstream.getYear().getId());
            if (optionalVoucher.isPresent()) {
                currentUpstream = optionalVoucher.get();
            } else {
                break;
            }
        }
        return updatedVouchers;
    }

    public List<VoucherLogDTO> sortVoucherListByUpdate(List<Voucher> vouchers) {
        // Find first voucher in list(The voucher with no updatedVoucher)
        Voucher firstVoucher = vouchers.getFirst();
        Integer position = 1;
        for (int x = 0; x < vouchers.size(); x++) {
            if (vouchers.get(x).getUpdatedVoucher() == null) {
                firstVoucher = vouchers.get(x);
            }
        }
        List<VoucherLogDTO> response = new ArrayList<>();
        response.add(Converter.convertToVoucherLog(firstVoucher, position));
        // Add upstream
        Voucher currentUpstream = firstVoucher;
        while (true) {
            // Check if id exist
            Voucher finalCurrentUpstream = currentUpstream;
            Voucher voucher = getVoucher(finalCurrentUpstream.getId(), finalCurrentUpstream.getYear().getId());
            if (!currentUpstream.equals(firstVoucher)) { // Exclude first voucher(already added downstream)
                response.add(Converter.convertToVoucherLog(voucher, position));
            }
            // Check if voucher is updated before
            Optional<Voucher> optionalVoucher = voucherRepository.findUpdatedVoucherIdByVoucherPK(
                    currentUpstream.getId(), currentUpstream.getYear().getId());
            if (optionalVoucher.isPresent()) {
                currentUpstream = optionalVoucher.get();
                position++;
            } else {
                break;
            }
        }
        return response;
    }

    public void saveVoucher(VoucherRequestDTO voucher) {
        // Save document if attached
        String filePath = Document.saveDocumentIfAttached(voucher);
        Voucher newVoucher = new Voucher(
                (voucher.getId() == null) ? voucherRepository.findLastAvailableVoucherIds(1, ((voucher.getYear() == null) ?
                        LocalDateTime.now().getYear() : voucher.getYear())).getFirst() : voucher.getId(),
                new Year(voucher.getYear()),
                null,
                false,
                false,
                filePath,
                null,
                LocalDateTime.now());
        voucherRepository.save(newVoucher);
        // Save postings
        voucher.getPostingDTO().forEach(posting -> {
            // Check if account exist
            Account account = accountService.getAccount(posting.getAccountId());
            postingRepository.save(
                    new Posting(
                            newVoucher,
                            posting.getDate(),
                            account,
                            posting.getAmount()
                    ));
        });
    }

    public Boolean isVoucherUpdated(Voucher voucher) {
        return (1 < getUpdatedVouchers(voucher).size());
    }

    public Voucher getVoucher(Integer id, Integer year) {
        return voucherRepository.findByVoucherPK(id, year).orElseThrow(
                () -> new EntityNotFoundException("Voucher or TempVoucher with id " + id + " does not exist in " + year));
    }

    public void checkIfVoucherExist(Integer id, Integer year) {
        voucherRepository.findByVoucherPK(id, year).ifPresent(x -> {
            throw new ConflictException("Voucher or temp-voucher with id " + id + " already exist in " + year);
        });
    }

    public VoucherResponseDTO saveNewPostings(VoucherRequestDTO voucher, Voucher newVoucher) {
        voucher.getPostingDTO().forEach(voucher2 -> {
            // Check if account exist
            Account account = accountService.getAccount(voucher2.getAccountId());
            Posting newPosting = new Posting(newVoucher, voucher2.getDate(),
                    account, voucher2.getAmount());
            postingRepository.save(newPosting);
        });
        return Converter.convertToVoucherResponse(
                getVoucherWithPostings(newVoucher.getId(), newVoucher.getYear().getId()),
                isVoucherUpdated(newVoucher)
        );
    }

    public VoucherWithPostingsDTO getVoucherWithPostings(Integer id, Integer year) {
        Voucher voucher = getVoucher(id, year);
        // Get postings based on voucher and convert to DTOs
        List<Posting> postings = postingRepository.findByVoucherPK(id, year);
        return new VoucherWithPostingsDTO(voucher, postings);
    }
}