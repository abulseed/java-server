package org.example.repository;

import org.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProductRepository {
    private final Map<UUID, Product> products = new HashMap<>();

    public Product save(Product product) {
        products.put(product.id(), product);
        return product;
    }

    public Optional<Product> findById(UUID id) {
        return Optional.ofNullable(products.get(id));
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public void deleteById(UUID id) {
        products.remove(id);
    }
}