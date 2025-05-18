package com.company.accountingsystem.voucher.tempvoucher;

import com.company.accountingsystem.voucher.dto.DocumentDTO;
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
@Schema(name = "TempVoucher")
public class TempVoucherDTO {
    @Schema(description = "The unique identifier of the voucher. If it is not set then it will use next available.", example = "1")
    private Integer tempId;

    @Schema(example = "2024", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer tempYear;

    @JsonProperty("document")
    private DocumentDTO documentDTO;

    @JsonProperty("postings")
    private List<PostingDTO> postingDTO;

    @Schema(description = "", example = "2025-05-11T20:38:19.6893819")
    private LocalDateTime TempCreatedOn;
}
