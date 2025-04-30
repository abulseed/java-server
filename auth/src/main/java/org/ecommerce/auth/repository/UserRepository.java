package org.ecommerce.auth.repository;

import org.ecommerce.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  /**
   * Find a user by their username.
   * 
   * @param username the username to look up
   * @return an Optional containing the User if found
   */
  Optional<User> findByUsername(String username);
}
