package com.emlakjet.purchasing.service;

import com.emlakjet.purchasing.dto.BaseResponse;
import com.emlakjet.purchasing.dto.BillDto;
import com.emlakjet.purchasing.entity.Bill;
import com.emlakjet.purchasing.entity.Product;
import com.emlakjet.purchasing.entity.User;
import com.emlakjet.purchasing.repository.BillRepository;
import com.emlakjet.purchasing.repository.ProductRepository;
import com.emlakjet.purchasing.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This is the class where Bill related operations are performed.
 */
@Slf4j
@Service
public class BillService {

    @Value("${purchase-limit}")
    private double limit;

    private final BillRepository billRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public BillService(BillRepository billRepo, ProductRepository productRepo, UserRepository userRepo) {
        this.billRepo = billRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    /**
     * This lists all (approved and rejected) bills by the given purchasing specialist id.
     *
     * @param specialistId The specialist id
     * @param page         The pagination page
     * @param size         The page size
     * @param sortDir      The pagination sort direction
     * @param sort         The pagination sorting parameter
     */
    public ResponseEntity<BaseResponse> listByPurchasingSpecialist(String specialistId, int page, int size, String sortDir, String sort) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sort);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", billRepo.findAllByPurchasingSpecialist_Id(UUID.fromString(specialistId), pageRequest)));
    }

    /**
     * This lists approved bills by the given purchasing specialist id.
     *
     * @param specialistId The specialist id
     * @param page         The pagination page
     * @param size         The page size
     * @param sortDir      The pagination sort direction
     * @param sort         The pagination sorting parameter
     */
    public ResponseEntity<BaseResponse> listApprovedByPurchasingSpecialist(String specialistId, int page, int size, String sortDir, String sort) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sort);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", billRepo.findAllByPurchasingSpecialist_IdAndApprovalStatusTrue(UUID.fromString(specialistId), pageRequest)));
    }

    /**
     * This lists rejected bills by the given purchasing specialist id.
     *
     * @param specialistId The specialist id
     * @param page         The pagination page
     * @param size         The page size
     * @param sortDir      The pagination sort direction
     * @param sort         The pagination sorting parameter
     */
    public ResponseEntity<BaseResponse> listRejectedByPurchasingSpecialist(String specialistId, int page, int size, String sortDir, String sort) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sort);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", billRepo.findAllByPurchasingSpecialist_IdAndApprovalStatusFalse(UUID.fromString(specialistId), pageRequest)));
    }

    /**
     * This returns the bill based on the given id parameter.
     *
     * @param id Bill id
     */
    public ResponseEntity<BaseResponse> get(UUID id) {
        Bill existingBill = billRepo.findById(id).orElse(null);
        if (existingBill == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse("No bill found for this id: " + id));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", existingBill));
    }

    /**
     * This creates a new bill.
     * The given Specialist and Product are checked.
     * If there is no such user and product, a not found response is returned.
     * If the bill amount does not exceed Specialist's total limit, the invoice is approved. It is recorded as approved in the database.
     * If the bill amount exceeds Specialist's total limit, the invoice is rejected. It is recorded as rejected in the database.
     *
     * @param billDto There are firstName, lastName, email, amount, productName and billNo fields for the bill.
     */
    public ResponseEntity<BaseResponse> create(BillDto billDto) {
        User user = userRepo.findByEmail(billDto.getEmail());
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse("No user found for this email: " + billDto.getEmail()));
        }

        Product product = productRepo.findByName(billDto.getProductName());
        if (product == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse("No product found for this name: " + billDto.getProductName()));
        }

        Double spendingTotal = billRepo.findByPurchasingSpecialist_IdAndApprovalStatusTrue(user.getId());
        if (spendingTotal == null) {
            spendingTotal = (double) 0;
        }
        boolean approvalStatus = (spendingTotal + billDto.getAmount()) < limit;

        Bill bill = new Bill(billDto.getBillNo(), billDto.getAmount(), approvalStatus, product, user);

        if (approvalStatus) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse("Successful", billRepo.save(bill)));
        }

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(new BaseResponse("The invoice was rejected because it exceeded the specialist limit.", billRepo.save(bill)));
    }

    /**
     * It soft deletes the bill belonging to the given bill id.
     *
     * @param id Bill id
     */
    public ResponseEntity<BaseResponse> delete(UUID id) {
        Bill existingBill = billRepo.findById(id).orElse(null);
        if (existingBill == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse("No bill found for this id: " + id));
        }

        existingBill.setDeleted(true);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponse("Successful", billRepo.save(existingBill)));
    }

}
