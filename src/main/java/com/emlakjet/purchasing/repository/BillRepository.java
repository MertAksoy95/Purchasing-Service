package com.emlakjet.purchasing.repository;

import com.emlakjet.purchasing.entity.Bill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID> {

    List<Bill> findAllByPurchasingSpecialist_Id(UUID id, Pageable pageable);

    List<Bill> findAllByPurchasingSpecialist_IdAndApprovalStatusTrue(UUID id, Pageable pageable);

    List<Bill> findAllByPurchasingSpecialist_IdAndApprovalStatusFalse(UUID id, Pageable pageable);

    @Query("select sum(b.amount)from Bill b where b.purchasingSpecialist.id = ?1 and b.approvalStatus = true")
    Double findByPurchasingSpecialist_IdAndApprovalStatusTrue(UUID id);

}
