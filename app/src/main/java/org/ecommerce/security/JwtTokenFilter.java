package org.ecommerce.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private RedisTemplate<String, Boolean> redis;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest req,
      @NonNull HttpServletResponse res,
      @NonNull FilterChain chain) throws ServletException, IOException {
    try {
      String header = req.getHeader("Authorization");
      if (header != null && header.startsWith("Bearer ")) {
        String token = header.substring(7);
        // Validate token, extract claims
        boolean valid = jwtUtil.validateToken(token);
        if (!valid) {
          res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          res.setContentType("application/json");
          res.getWriter().write("{\"error\": \"Invalid or expired token\"}");
          return;
        }

        String id = jwtUtil.extractId(token);
        // Revocation check
        if (redis.hasKey("revoked:" + id)) {
          res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          res.setContentType("application/json");
          res.getWriter().write("{\"error\": \"Revoked token\"}");
          return;
        }

        String username = jwtUtil.extractUsername(token);
        List<String> roles = jwtUtil.extractRoles(token);
        var auth = new UsernamePasswordAuthenticationToken(
            username, null, roles.stream().map(SimpleGrantedAuthority::new).toList());
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (Exception e) {
      log.error("Cannot set user authentication: {}", e);
    }
    chain.doFilter(req, res);
  }
}
