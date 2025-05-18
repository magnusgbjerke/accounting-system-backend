package com.company.accountingsystem.year;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Year {
    @Schema(example = "2024")
    @Id
    private Integer id;
}
