package com.emlakjet.purchasing.controller;

import com.emlakjet.purchasing.dto.BaseResponse;
import com.emlakjet.purchasing.dto.BillDto;
import com.emlakjet.purchasing.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * This is the class where we handle Bill related requests. API details are available in the service layer.
 */
@Slf4j
@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @Operation(summary = "This is to list all bills by purchasing specialist.")
    @GetMapping("/purchasing-specialist/{specialistId}")
    public ResponseEntity<BaseResponse> listByPurchasingSpecialist(@PathVariable String specialistId,
                                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                                   @RequestParam(required = false, defaultValue = "10") int size,
                                                                   @RequestParam(required = false, defaultValue = "desc") String sortDir,
                                                                   @RequestParam(required = false, defaultValue = "created") String sort) {
        return billService.listByPurchasingSpecialist(specialistId, page, size, sortDir, sort);
    }

    @Operation(summary = "This is to list all approved bills by purchasing specialist.")
    @GetMapping("/purchasing-specialist/{specialistId}/approved")
    public ResponseEntity<BaseResponse> listApprovedByPurchasingSpecialist(@PathVariable String specialistId,
                                                                           @RequestParam(required = false, defaultValue = "0") int page,
                                                                           @RequestParam(required = false, defaultValue = "10") int size,
                                                                           @RequestParam(required = false, defaultValue = "desc") String sortDir,
                                                                           @RequestParam(required = false, defaultValue = "created") String sort) {
        return billService.listApprovedByPurchasingSpecialist(specialistId, page, size, sortDir, sort);
    }

    @Operation(summary = "This is to list all rejected bills by purchasing specialist.")
    @GetMapping("/purchasing-specialist/{specialistId}/rejected")
    public ResponseEntity<BaseResponse> listRejectedByPurchasingSpecialist(@PathVariable String specialistId,
                                                                           @RequestParam(required = false, defaultValue = "0") int page,
                                                                           @RequestParam(required = false, defaultValue = "10") int size,
                                                                           @RequestParam(required = false, defaultValue = "desc") String sortDir,
                                                                           @RequestParam(required = false, defaultValue = "created") String sort) {
        return billService.listRejectedByPurchasingSpecialist(specialistId, page, size, sortDir, sort);
    }

    @Operation(summary = "This is to get bill by Id.")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "This is when the bill is not found.")})
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> get(@PathVariable UUID id) {
        return billService.get(id);
    }

    @Operation(summary = "This is to creating bill.")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "This is when the user or product are not found."),})
    @PostMapping
    public ResponseEntity<BaseResponse> create(@RequestBody BillDto billDto) {
        return billService.create(billDto);
    }

    @Operation(summary = "This is to deleting the bill.")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "This is when the bill is not found.")})
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable UUID id) {
        return billService.delete(id);
    }

}
