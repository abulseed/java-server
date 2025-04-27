package org.example.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record Product(
    UUID id,
    String name,
    String description,
    BigDecimal price,
    int quantity,
    List<String> photoUrls
) {}