package com.company.accountingsystem.voucher;

import com.company.accountingsystem.year.Year;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
@IdClass(VoucherPK.class)
public class Voucher {
    @Id
    public Integer id;

    @ManyToOne
    @Id
    public Year year;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "voucher_id_fk"),
            @JoinColumn(name = "voucher_year_fk")
    })
    private Voucher updatedVoucher;

    @Column(nullable = false)
    private Boolean updateVoucher;

    @Column(nullable = false)
    private Boolean reverseVoucher;

    private String filePath;

    private LocalDateTime tempCreatedOn;

    private LocalDateTime createdOn;
}