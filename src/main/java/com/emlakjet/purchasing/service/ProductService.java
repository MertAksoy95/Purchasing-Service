package com.emlakjet.purchasing.service;

import com.emlakjet.purchasing.dto.BaseResponse;
import com.emlakjet.purchasing.dto.ProductDto;
import com.emlakjet.purchasing.entity.Product;
import com.emlakjet.purchasing.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This is the class where Product related operations are performed.
 */
@Slf4j
@Service
public class ProductService {

    private final ModelMapper mapper;
    private final ProductRepository productRepo;

    public ProductService(ModelMapper mapper, ProductRepository productRepo) {
        this.mapper = mapper;
        this.productRepo = productRepo;
    }


    /**
     * This lists all products.
     *
     * @param page    The pagination page
     * @param size    The page size
     * @param sortDir The pagination sort direction
     * @param sort    The pagination sorting parameter
     */
    public ResponseEntity<BaseResponse> list(int page, int size, String sortDir, String sort) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sort);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", productRepo.findAll(pageRequest)));
    }

    /**
     * This returns the product based on the given id parameter.
     *
     * @param id Product id
     */
    public ResponseEntity<BaseResponse> get(UUID id) {
        Product existingProduct = productRepo.findById(id).orElse(null);
        if (existingProduct == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse("No product found for this id: " + id));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", existingProduct));
    }

    /**
     * This creates a new product.
     * Product names are unique.
     * If there is a product with the same name, it returns a conflict response.
     *
     * @param productDto There are name, description and price fields for the product.
     */
    public ResponseEntity<BaseResponse> create(ProductDto productDto) {
        Product existingProduct = productRepo.findByName(productDto.getName());
        if (existingProduct != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new BaseResponse("This product name is already in use: " + productDto.getName()));
        }

        Product product = mapper.map(productDto, Product.class);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", productRepo.save(product)));
    }

    /**
     * This updates the existing product.
     * Product names are unique.
     * If there is a product with the same name, it returns a conflict response.
     *
     * @param id         Product id
     * @param productDto There are name, description and price fields for the product.
     */
    public ResponseEntity<BaseResponse> update(UUID id, ProductDto productDto) {
        Product existingProduct = productRepo.findById(id).orElse(null);
        if (existingProduct == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse("No product found for this id: " + id));
        }

        if (!existingProduct.getName().equalsIgnoreCase(productDto.getName())) {
            Product product = productRepo.findByName(productDto.getName());
            if (product != null) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new BaseResponse("This product name is already in use: " + productDto.getName()));
            }
        }

        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", productRepo.save(existingProduct)));
    }

    /**
     * It soft deletes the product belonging to the given product id.
     *
     * @param id Product id
     */
    public ResponseEntity<BaseResponse> delete(UUID id) {
        Product existingProduct = productRepo.findById(id).orElse(null);
        if (existingProduct == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse("No product found for this id: " + id));
        }

        existingProduct.setDeleted(true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", productRepo.save(existingProduct)));
    }

}
