package com.company.accountingsystem.voucher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(name = "Document")
public class DocumentDTO {
    @Schema(description = "The document Base64 encoded.")
    private String fileData;

    @Schema(description = "The name of the file.", example = "Restaurantregning.pdf")
    private String fileName;
}
