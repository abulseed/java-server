package org.ecommerce.repository;

import org.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}