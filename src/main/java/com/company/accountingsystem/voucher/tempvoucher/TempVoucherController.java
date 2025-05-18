package com.company.accountingsystem.voucher.tempvoucher;

import com.company.accountingsystem.voucher.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/voucher/temp")
@Tag(name = "temp-vouchers", description = "The Temporarily Voucher API")
public class TempVoucherController {

    private final TempVoucherService tempVoucherService;

    public TempVoucherController(TempVoucherService tempVoucherService) {
        this.tempVoucherService = tempVoucherService;
    }

    @Operation(summary = "Get all temporarily vouchers", description = "Retrieves a list of all TempVouchers", tags = {"temp-vouchers"})
    @ApiResponses(value = {@ApiResponse(
            description = "successful operation",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TempVoucherDTO.class))})})
    @GetMapping
    public ResponseEntity<List<TempVoucherDTO>> getTempVouchers() {
        return tempVoucherService.getTempVouchers();
    }

    @Operation(summary = "Post a temporarily voucher", description = "Posts a TempVoucher. Responds with posted voucher", tags = {"temp-vouchers"})
    @ApiResponses(value = {@ApiResponse(
            description = "successful operation",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = VoucherResponseDTO.class))})})
    @PostMapping(path = "/{tempId}-{tempYear}")
    public ResponseEntity<VoucherResponseDTO> postTempVoucher(@PathVariable("tempId") Integer tempId,
                                                              @PathVariable("tempYear") Integer tempYear) {
        return tempVoucherService.postTempVoucher(tempId, tempYear);
    }

    @Operation(summary = "Create a temporarily voucher", description = "Creates a TempVoucher", tags = {"temp-vouchers"})
    @ApiResponses(value = {@ApiResponse(
            description = "successful operation",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TempVoucherDTO.class))})})
    @PostMapping
    public ResponseEntity<TempVoucherDTO> createTempVoucher(@RequestBody TempVoucherDTO voucher) {
        return tempVoucherService.createTempVoucher(voucher);
    }

    @Operation(summary = "Update a temporarily voucher", description = "Updates a TempVoucher.", tags = {"temp-vouchers"})
    @ApiResponses(value = {@ApiResponse(
            description = "successful operation",
            responseCode = "200"
    )})
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public void updateTempVoucher(@RequestBody TempVoucherDTO voucher) {
        tempVoucherService.updateTempVoucher(voucher);
    }

    @Operation(summary = "Delete a temporarily voucher", description = "Deletes a TempVoucher based on tempId and tempYear. Responds with code 200", tags = {"temp-vouchers"})
    @ApiResponses(value = {@ApiResponse(
            description = "successful operation",
            responseCode = "200"
    )})
    @DeleteMapping(path = "/{tempId}-{tempYear}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteTempVoucher(@PathVariable("tempId") Integer tempId,
                                  @PathVariable("tempYear") Integer tempYear) {
        tempVoucherService.deleteTempVoucher(tempId, tempYear);
    }
}