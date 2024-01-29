package com.emlakjet.purchasing.entity;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@Where(clause = "deleted = false")
public class Product extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    private double price;

}
