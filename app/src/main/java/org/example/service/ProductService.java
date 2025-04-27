package org.example.service;

import org.example.model.Product;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product createProduct(String name, String description, java.math.BigDecimal price, int quantity, List<String> photoUrls) {
        Product product = new Product(UUID.randomUUID(), name, description, price, quantity, photoUrls);
        return repository.save(product);
    }

    public Optional<Product> getProductById(UUID id) {
        return repository.findById(id);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Optional<Product> updateProduct(UUID id, String name, String description, java.math.BigDecimal price, int quantity, List<String> photoUrls) {
        Product updated = new Product(id, name, description, price, quantity, photoUrls);
        return repository.findById(id)
                         .map(p -> repository.save(updated));
    }

    public void deleteProduct(UUID id) {
        repository.deleteById(id);
    }
}