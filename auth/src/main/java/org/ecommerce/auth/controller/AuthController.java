package org.ecommerce.auth.controller;

import org.ecommerce.auth.model.User;
import org.ecommerce.auth.service.UserService;
import org.ecommerce.auth.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final UserService userService;
  private final JwtUtil jwtUtil;

  /**
   * Registers a new user.
   * Expects JSON: { "username": "...", "password": "..." }
   * Returns 200 OK with { "id": "<newUserId>" }.
   */
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Map<String, Object> req) {
    try {
      String username = (String) req.get("username");
      String password = (String) req.get("password");
      List<?> raw = (List<?>) req.get("roles");
      List<String> roles = raw == null ? null
          : raw.stream().map(Object::toString).collect(Collectors.toList());

      User created = userService.registerUser(username, password, roles);
      return ResponseEntity.ok(Map.of("id", created.getId()));
    } catch (Exception e) {
      log.error("Error registering new user: ", e);
      return ResponseEntity.badRequest().body(
          "Error registering new user: " + e.getMessage());
    }
  }

  /**
   * Authenticates a user.
   * Expects JSON: { "username": "...", "password": "..." }
   * Returns 200 OK with { "token": "<jwt>" } if successful,
   * or 401 Unauthorized on failure.
   */
  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> creds) {
    String username = creds.get("username");
    String password = creds.get("password");

    UserDetails authenticated = userService.authenticate(username, password);
    if (authenticated == null) {
      return ResponseEntity.status(401).build();
    }

    List<String> roles = authenticated.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    String token = jwtUtil.generateToken(authenticated.getUsername(), roles);
    return ResponseEntity.ok(Map.of("token", token));
  }
}
