package com.company.accountingsystem.voucher;

import com.company.accountingsystem.exception.ConflictException;
import com.company.accountingsystem.voucher.dto.*;
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
public class VoucherService {
    private final YearService yearService;
    private final HelperService helperService;
    private final VoucherRepository voucherRepository;

    public VoucherService(YearService yearService,
                          HelperService helperService,
                          VoucherRepository voucherRepository) {
        this.yearService = yearService;
        this.helperService = helperService;
        this.voucherRepository = voucherRepository;
    }

    public ResponseEntity<VoucherResponseDTO> getVoucher(Integer id, Integer year) {
        VoucherWithPostingsDTO voucher = helperService.getVoucherWithPostings(id, year);
        return new ResponseEntity<>(
                Converter.convertToVoucherResponse(
                        voucher,
                        helperService.isVoucherUpdated(voucher.getVoucher())
                ), HttpStatus.OK);
    }

    public ResponseEntity<List<VoucherResponseDTO>> getVouchers() {
        List<VoucherResponseDTO> response = new ArrayList<>();
        // Get all vouchers by posted
        List<Voucher> allVouchers = voucherRepository.findAllByPostedOrNotPosted(true);
        // Get posting for each voucher
        allVouchers.forEach(voucher -> {
            VoucherWithPostingsDTO vwp = helperService.getVoucherWithPostings(voucher.getId(), voucher.getYear().getId());
            response.add(Converter.convertToVoucherResponse(vwp, helperService.isVoucherUpdated(vwp.getVoucher())));
        });
        // Sort response by id
        response.sort(Comparator.comparing(VoucherResponseDTO::getId));
        // Sort response by year
        response.sort(Comparator.comparing(VoucherResponseDTO::getYear));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<List<VoucherResponseDTO>> createVouchers(List<VoucherRequestDTO> vouchers) {
        // Check if id already-exist and if year exist
        vouchers.forEach(voucher -> {
            helperService.checkIfVoucherExist(voucher.getId(), voucher.getYear());
            yearService.getYear(voucher.getYear());
        });
        // Validate each voucher
        vouchers.forEach(voucher -> {
            Validate.validateVoucher(voucher);
        });
        // Save each voucher
        vouchers.forEach(voucher2 -> {
            helperService.saveVoucher(voucher2);
        });
        // Make response
        List<VoucherResponseDTO> response = new ArrayList<>();
        vouchers.forEach(voucher -> {
            VoucherWithPostingsDTO vwp = helperService.getVoucherWithPostings(voucher.getId(), voucher.getYear());
            response.add(
                    Converter.convertToVoucherResponse(
                            vwp,
                            helperService.isVoucherUpdated(vwp.getVoucher())
                    )
            );
        });
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<VoucherResponseDTO> deleteVoucher(Integer id, Integer year) {
        Voucher voucher = helperService.getVoucher(id, year);
        // Check if voucher is updated before
        voucherRepository.findUpdatedVoucherIdByVoucherPK(voucher.getId(), voucher.getYear().getId()).ifPresent(x -> {
            throw new ConflictException("It is not possible to reverse a voucher that is already reversed or updated. Voucher " +
                    voucher.getId() + " is updated or reversed by voucher " + x.getId() + " in " + x.getYear().getId());
        });
        // Check if voucher is a TempVoucher
        if (voucher.getCreatedOn() == null) {
            throw new ConflictException("It is not possible to reverse a TempVoucher.");
        }
        // Check if voucher is a reverse-voucher(Only on delete-endpoint)
        if (voucher.getReverseVoucher()) {
            throw new ConflictException("It is not possible to reverse a ReverseVoucher");
        }
        // Reverse voucher
        Voucher reverseVoucher = helperService.reverseVoucher(id, year);
        // Make response
        VoucherResponseDTO response = Converter.convertToVoucherResponse(
                helperService.getVoucherWithPostings(
                        reverseVoucher.getId(),
                        reverseVoucher.getYear().getId()
                ),
                helperService.isVoucherUpdated(reverseVoucher)
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<VoucherResponseDTO> updateVoucher(VoucherRequestDTO voucher) {
        Voucher voucher2 = helperService.getVoucher(voucher.getId(), voucher.getYear());
        // Check if voucher is updated before
        voucherRepository.findUpdatedVoucherIdByVoucherPK(voucher2.getId(), voucher2.getYear().getId()).ifPresent(x -> {
            throw new ConflictException("It is not possible to update a voucher that is already reversed or updated. Voucher " +
                    voucher.getId() + " is updated or reversed by voucher " + x.getId() + " in " + x.getYear().getId());
        });
        // Check if voucher is a TempVoucher
        if (voucher2.getCreatedOn() == null) {
            throw new ConflictException("It is not possible to update a TempVoucher.");
        }
        // Validate voucher
        Validate.validateVoucher(voucher);
        // If voucher is a reverse-voucher, then only create new voucher
        if (voucher2.getReverseVoucher()) {
            // Save document if attached
            String filePath = Document.saveDocumentIfAttached(voucher);
            // Create and save new voucher with new details
            Voucher newVoucher = new Voucher(
                    voucherRepository.findLastAvailableVoucherIds(1, voucher2.getYear().getId()).getFirst(), // New id based on next available voucher id
                    new Year(voucher.getYear()),
                    voucher2,
                    true,
                    false,
                    filePath,
                    null,
                    LocalDateTime.now()
            );
            voucherRepository.save(newVoucher);
            // Create and save new postings with new details
            VoucherResponseDTO response = helperService.saveNewPostings(voucher, newVoucher);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // Reverse voucher
            Voucher reverseVoucher = helperService.reverseVoucher(voucher.getId(), voucher.getYear());
            // Save document if attached
            String filePath = Document.saveDocumentIfAttached(voucher);
            // Create and save new voucher with new details
            Voucher newVoucher = new Voucher(
                    voucherRepository.findLastAvailableVoucherIds(1, voucher2.getYear().getId()).getFirst(), // New id based on next available voucher id
                    new Year(voucher.getYear()),
                    reverseVoucher,
                    true,
                    false,
                    filePath,
                    null,
                    LocalDateTime.now()
            );
            voucherRepository.save(newVoucher);
            // Create and save new postings with new details
            VoucherResponseDTO response = helperService.saveNewPostings(voucher, newVoucher);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    public ResponseEntity<List<VoucherLogDTO>> getVoucherLog(Integer id, Integer year) {
        Voucher voucher = helperService.getVoucher(id, year);
        // Check if voucher is a TempVoucher
        if (voucher.getCreatedOn() == null) {
            throw new ConflictException("It is not possible to get log of a TempVoucher.");
        }
        List<Voucher> updatedVouchers = helperService.getUpdatedVouchers(voucher);
        return new ResponseEntity<>(helperService.sortVoucherListByUpdate(updatedVouchers), HttpStatus.OK);
    }
}



