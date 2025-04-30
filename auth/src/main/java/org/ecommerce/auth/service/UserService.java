package org.ecommerce.auth.service;

import org.ecommerce.auth.model.User;
import org.ecommerce.auth.repository.UserRepository;
import org.ecommerce.auth.security.JwtUtil;
import org.ecommerce.auth.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

  @Autowired
  private final UserRepository userRepository;
  @Autowired
  private final PasswordEncoder passwordEncoder;
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  JwtUtil jwtUtil;

  /**
   * Register a brand-new user with a hashed password.
   */
  public User registerUser(String username, String rawPassword, List<String> roles) throws Exception {
    if (findByUsername(username).isPresent()) {
      throw new Exception("Username is already taken!");
    }
    String hashed = passwordEncoder.encode(rawPassword);
    User user = new User(UUID.randomUUID(), username, hashed, roles);
    return userRepository.save(user);
  }

  /**
   * Look up a user by username.
   */
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  /**
   * Verify a username/password combination.
   */
  public UserDetails authenticate(String username, String rawPassword) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, rawPassword));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    return userDetails;
  }
}
