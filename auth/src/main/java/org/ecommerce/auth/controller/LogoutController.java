package org.ecommerce.auth.controller;

import java.util.Date;

import org.ecommerce.auth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/logout")
@RequiredArgsConstructor
@Slf4j
public class LogoutController {

  @Autowired
  JwtUtil jwtUtil;

  @Autowired
  private RedisTemplate<String, Boolean> redis;

  @PostMapping
  public ResponseEntity<?> logout(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      String jti = jwtUtil.extractId(token);
      Date expirationDate = jwtUtil.extractExpiration(token);
      long nowMillis = System.currentTimeMillis();
      long expMillis = expirationDate.getTime();
      long ttlSeconds = (expMillis - nowMillis) / 1000;
      if (ttlSeconds < 0)
        ttlSeconds = 0;
      redis.opsForValue().set("revoked:" + jti, true, ttlSeconds, java.util.concurrent.TimeUnit.SECONDS);
      return ResponseEntity.ok("{\"message\": \"Logged out\"}");
    }
    return ResponseEntity.badRequest().body("{\"error\": \"No token provided\"}");
  }
}
