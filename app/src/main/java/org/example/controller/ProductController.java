package org.example.controller;

import org.example.model.Product;
import org.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        String description = (String) request.get("description");
        BigDecimal price = NumberUtils.parseNumber((String) request.get("price"), BigDecimal.class);
        int quantity = NumberUtils.parseNumber((String) request.get("quantity"), Integer.class);
        List<?> photoUrlsRaw = (List<?>) request.get("photoUrls");
        List<String> photoUrls = photoUrlsRaw == null ? null
                : photoUrlsRaw.stream().map(Object::toString).collect(Collectors.toList());

        Product product = new Product(UUID.randomUUID(), name, description, price, quantity, photoUrls);
        service.createProduct(product);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") final UUID id) {
        return service.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Product> getAll() {
        return service.getAllProducts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable UUID id, @RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        String description = (String) request.get("description");
        BigDecimal price = new BigDecimal(request.get("price").toString());
        int quantity = (int) request.get("quantity");
        List<?> photoUrlsRaw = (List<?>) request.get("photoUrls");
        List<String> photoUrls = photoUrlsRaw == null ? null
                : photoUrlsRaw.stream().map(Object::toString).collect(Collectors.toList());

        Product product = new Product(id, name, description, price, quantity, photoUrls);
        return service.updateProduct(product)
                      .map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        service.deleteProduct(id);
        return ResponseEntity.ok().body("Deleted product successfully");
    }
}