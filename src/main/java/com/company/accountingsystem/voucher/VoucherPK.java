package com.company.accountingsystem.voucher;

import com.company.accountingsystem.year.Year;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoucherPK implements Serializable {
    public Integer id;
    @ManyToOne
    public Year year;
}

