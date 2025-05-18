package com.company.accountingsystem.voucher.dto;

import com.company.accountingsystem.voucher.posting.PostingDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(name = "VoucherResponse")
public class VoucherResponseDTO {
    @Schema(description = "The unique identifier of the voucher.", example = "1")
    private Integer id;

    @Schema(example = "2024")
    private Integer year;

    @Schema(description = "If the voucher has been updated", example = "false")
    private Boolean updated;

    @Schema(description = "If the voucher is a reverse-voucher", example = "1")
    private Boolean reverseVoucher;

    @JsonProperty("document")
    private DocumentDTO documentDTO;

    @JsonProperty("postings")
    private List<PostingDTO> postingDTO;

    @Schema(description = "", example = "2025-05-11T20:38:19.6893819")
    private LocalDateTime createdOn;
}
