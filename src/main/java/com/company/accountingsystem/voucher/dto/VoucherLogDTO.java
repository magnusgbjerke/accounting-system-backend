package com.company.accountingsystem.voucher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(name = "VoucherLog")
public class VoucherLogDTO {
    @Schema(description = "The unique identifier of the voucher.", example = "1")
    private Integer id;

    @Schema(example = "2024")
    private Integer year;

    @Schema(example = "1")
    private Integer position;

    @Schema(example = "false")
    private Boolean updateVoucher;

    @Schema(description = "If the voucher is a reverse-voucher", example = "1")
    private Boolean reverseVoucher;

    @Schema(description = "", example = "2025-05-11T20:38:19.6893819")
    private LocalDateTime tempCreatedOn;

    @Schema(description = "", example = "2025-05-11T20:38:19.6893819")
    private LocalDateTime createdOn;
}