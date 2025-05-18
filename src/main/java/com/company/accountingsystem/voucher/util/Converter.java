package com.company.accountingsystem.voucher.util;

import com.company.accountingsystem.voucher.Voucher;
import com.company.accountingsystem.voucher.dto.*;
import com.company.accountingsystem.voucher.posting.PostingDTO;
import com.company.accountingsystem.voucher.tempvoucher.TempVoucherDTO;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    public static List<PostingDTO> convertToPostings(VoucherWithPostingsDTO voucherWithPostingsDTO) {
        List<PostingDTO> postings = new ArrayList<>();
        voucherWithPostingsDTO.getPostings().forEach(posting -> {
            postings.add(new PostingDTO(posting.getId(), posting.getDate(), posting.getAccount().getId(), posting.getAmount())
            );
        });
        return postings;
    }

    public static VoucherResponseDTO convertToVoucherResponse(VoucherWithPostingsDTO voucherWithPostingsDTO, Boolean updated) {
        List<PostingDTO> postings = convertToPostings(voucherWithPostingsDTO);
        return new VoucherResponseDTO(
                voucherWithPostingsDTO.getVoucher().getId(),
                voucherWithPostingsDTO.getVoucher().getYear().getId(),
                updated,
                voucherWithPostingsDTO.getVoucher().getReverseVoucher(),
                Document.getDocumentDTO(voucherWithPostingsDTO.getVoucher().getFilePath()),
                postings,
                voucherWithPostingsDTO.getVoucher().getCreatedOn());
    }

    public static TempVoucherDTO convertToTempVoucher(VoucherWithPostingsDTO voucherWithPostingsDTO) {
        List<PostingDTO> postings = convertToPostings(voucherWithPostingsDTO);
        return new TempVoucherDTO(
                voucherWithPostingsDTO.getVoucher().getId(),
                voucherWithPostingsDTO.getVoucher().getYear().getId(),
                Document.getDocumentDTO(voucherWithPostingsDTO.getVoucher().getFilePath()),
                postings,
                voucherWithPostingsDTO.getVoucher().getTempCreatedOn()
        );
    }

    public static VoucherLogDTO convertToVoucherLog(Voucher voucher, Integer position) {
        return new VoucherLogDTO(
                voucher.getId(),
                voucher.getYear().getId(),
                position,
                voucher.getUpdateVoucher(),
                voucher.getReverseVoucher(),
                voucher.getTempCreatedOn(),
                voucher.getCreatedOn()
        );
    }
}
