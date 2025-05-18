package com.company.accountingsystem.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    @Schema(example = "1920")
    @NotNull(message = "Id is required")
    @Max(value = 8000, message = "Account can never exceed 8000.")
    @Min(value = 1000, message = "Account can never be below 1000.")
    @Id
    private Integer id;

    @Schema(example = "Bank")
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;
}
