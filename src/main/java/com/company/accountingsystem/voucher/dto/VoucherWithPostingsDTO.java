package com.company.accountingsystem.voucher.dto;

import com.company.accountingsystem.voucher.Voucher;
import com.company.accountingsystem.voucher.posting.Posting;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoucherWithPostingsDTO {
    private Voucher voucher;
    private List<Posting> postings;
}
