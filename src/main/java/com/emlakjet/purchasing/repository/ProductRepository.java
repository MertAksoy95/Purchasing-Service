package com.emlakjet.purchasing.repository;

import com.emlakjet.purchasing.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Product findByName(String name);

}
