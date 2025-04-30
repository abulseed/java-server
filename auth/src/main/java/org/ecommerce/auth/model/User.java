package org.ecommerce.auth.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
  @Id
  private UUID id;

  private String username;

  private String password;

  private List<String> roles;
}
