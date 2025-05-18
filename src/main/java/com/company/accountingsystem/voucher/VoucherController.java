package com.company.accountingsystem.voucher;

import com.company.accountingsystem.voucher.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/voucher")
@Tag(name = "vouchers", description = "The Voucher API")
public class VoucherController {

    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @Operation(summary = "Get all vouchers", description = "Retrieves a list of all vouchers", tags = {"vouchers"})
    @ApiResponses(value = {@ApiResponse(
            description = "successful operation",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VoucherResponseDTO.class))})})
    @GetMapping
    public ResponseEntity<List<VoucherResponseDTO>> getVouchers() {
        return voucherService.getVouchers();
    }

    @Operation(summary = "Update a voucher", description = "Updates a voucher. If the updated voucher is a reverse-voucher, it will only create a new voucher. If it is not a reverse-voucher then it will reverse the old voucher and create a new voucher. Responds with updated voucher", tags = {"vouchers"})
    @ApiResponses(value = {@ApiResponse(
            description = "successful operation",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VoucherResponseDTO.class))})})
    @PutMapping
    public ResponseEntity<VoucherResponseDTO> updateVoucher(@RequestBody VoucherRequestDTO voucher) {
        return voucherService.updateVoucher(voucher);
    }

    @Operation(summary = "Create vouchers", description = "Creates vouchers", tags = {"vouchers"})
    @ApiResponses(value = {@ApiResponse(
            description = "successful operation",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VoucherResponseDTO.class))})})
    @PostMapping
    public ResponseEntity<List<VoucherResponseDTO>> createVouchers(@RequestBody List<VoucherRequestDTO> vouchers) {
        return voucherService.createVouchers(vouchers);
    }

    @Operation(summary = "Get a voucher", description = "Retrieves a voucher", tags = {"vouchers"})
    @ApiResponses(value = {@ApiResponse(
            description = "successful operation",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VoucherResponseDTO.class))})})
    @GetMapping(path = "/{id}-{year}")
    public ResponseEntity<VoucherResponseDTO> getVoucher(@PathVariable("id") Integer id,
                                                         @PathVariable("year") Integer year) {
        return voucherService.getVoucher(id, year);
    }

    @Operation(summary = "Delete a voucher", description = "Deletes(Reverses) a voucher. Responds with reversed voucher", tags = {"vouchers"})
    @ApiResponses(value = {@ApiResponse(
            description = "successful operation",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VoucherResponseDTO.class))})})
    @DeleteMapping(path = "/{id}-{year}")
    public ResponseEntity<VoucherResponseDTO> deleteVoucher(@PathVariable("id") Integer id,
                                                            @PathVariable("year") Integer year) {
        return voucherService.deleteVoucher(id, year);
    }

    @Operation(summary = "Get log of a voucher", description = "Retrieves log of a voucher. Gets all related vouchers. Position 1=first voucher, Position 2=second voucher..", tags = {"vouchers"})
    @ApiResponses(value = {@ApiResponse(
            description = "successful operation",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VoucherLogDTO.class))})})
    @GetMapping(path = "/{id}-{year}/log")
    public ResponseEntity<List<VoucherLogDTO>> getVoucherLog(@PathVariable("id") Integer id,
                                                             @PathVariable("year") Integer year) {
        return voucherService.getVoucherLog(id, year);
    }
}
