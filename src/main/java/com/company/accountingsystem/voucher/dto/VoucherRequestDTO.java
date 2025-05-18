package com.company.accountingsystem.voucher.dto;

import com.company.accountingsystem.voucher.posting.PostingDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(name = "VoucherRequest")
public class VoucherRequestDTO {
    @Schema(description = "The unique identifier of the voucher. If it is not set then it will use next available.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Schema(example = "2024", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer year;

    @JsonProperty("document")
    private DocumentDTO documentDTO;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("postings")
    private List<PostingDTO> postingDTO;
}
