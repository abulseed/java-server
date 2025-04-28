package org.ecommerce.service;

import org.ecommerce.model.Product;
import org.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;

    public Product createProduct(Product product) {
        return repository.save(product);
    }

    public Optional<Product> getProductById(UUID id) {
        return repository.findById(id);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Optional<Product> updateProduct(Product product) {
        return repository.findById(product.getId())
                .map(p -> repository.save(product));
    }

    public void deleteProduct(UUID id) {
        repository.deleteById(id);
    }
}