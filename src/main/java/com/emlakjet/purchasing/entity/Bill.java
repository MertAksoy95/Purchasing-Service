package com.emlakjet.purchasing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Where(clause = "deleted = false")
public class Bill extends BaseEntity {

    private String billNo;
    private double amount;
    private boolean approvalStatus;

    @ManyToOne
    private Product product;
    @ManyToOne
    private User purchasingSpecialist;

}
