package com.company.accountingsystem.voucher.posting;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@ToString
@Getter
@Setter
@Schema(name = "Posting")
@AllArgsConstructor
public class PostingDTO {
    @Schema(example = "1")
    private Integer id;

    @Schema(example = "2024-02-02")
    @NotNull(message = "Date can not be null.")
    private LocalDate date;

    @Schema(example = "1920")
    @Max(value = 8000, message = "Account can never exceed 8000.")
    @Min(value = 1000, message = "Account can never be below 1000.")
    @NotNull(message = "Account can not be null.")
    private Integer accountId;

    @Schema(example = "100")
    @NotNull(message = "Amount can not be null.")
    @Digits(integer = 10, fraction = 2, message = "Only two digits after decimal separator is allowed.")
    private BigDecimal amount;
}
