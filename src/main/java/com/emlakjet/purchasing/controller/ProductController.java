package com.emlakjet.purchasing.controller;

import com.emlakjet.purchasing.dto.BaseResponse;
import com.emlakjet.purchasing.dto.ProductDto;
import com.emlakjet.purchasing.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * This is the class where we handle Product related requests. API details are available in the service layer.
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(summary = "This is to list all products.")
    @GetMapping
    public ResponseEntity<BaseResponse> list(@RequestParam(required = false, defaultValue = "0") int page,
                                             @RequestParam(required = false, defaultValue = "10") int size,
                                             @RequestParam(required = false, defaultValue = "desc") String sortDir,
                                             @RequestParam(required = false, defaultValue = "created") String sort) {
        return productService.list(page, size, sortDir, sort);
    }

    @Operation(summary = "This is to get product by Id.")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "This is when the product is not found.")})
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> get(@PathVariable UUID id) {
        return productService.get(id);
    }

    @Operation(summary = "This is to creating product.")
    @PostMapping
    public ResponseEntity<BaseResponse> create(@RequestBody ProductDto productDto) {
        return productService.create(productDto);
    }

    @Operation(summary = "This is to updating the product.")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "This is when the product is not found.")})
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> update(@PathVariable UUID id, @RequestBody ProductDto productDto) {
        return productService.update(id, productDto);
    }

    @Operation(summary = "This is to deleting the product.")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", description = "This is when the product is not found.")})
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable UUID id) {
        return productService.delete(id);
    }

}
