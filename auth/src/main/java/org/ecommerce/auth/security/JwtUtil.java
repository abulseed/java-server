package org.ecommerce.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

  private final Key signingKey;
  private final long expirationMillis;

  public JwtUtil(
      @Value("${app.jwt.secret}") String secret,
      @Value("${app.jwt.expiration}") long expirationMillis) {
    // Use HS256; for HS512, change SignatureAlgorithm.HS512 and key size
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    this.expirationMillis = expirationMillis;
  }

  /**
   * Generate a JWT containing username and roles as claims, valid for configured
   * expiration
   */
  public String generateToken(String username, List<String> roles) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMillis);

    return Jwts.builder()
        .setSubject(username)
        .claim("roles", roles)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(signingKey, SignatureAlgorithm.HS256)
        .compact();
  }

  /** Validate token signature and expiration */
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(signingKey)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      // log or handle expired/invalid token
      return false;
    }
  }

  /** Extract username (subject) from token */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /** Extract id from token */
  public String extractId(String token) {
    return extractClaim(token, Claims::getId);
  }

  /** Extract roles claim from token */
  @SuppressWarnings("unchecked")
  public List<String> extractRoles(String token) {
    Claims claims = extractAllClaims(token);
    return claims.get("roles", List.class);
  }

  /** Generic claim extractor */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /** Parse all claims from token */
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(signingKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  /** Check if token has expired */
  public boolean isTokenExpired(String token) {
    Date expiration = extractClaim(token, Claims::getExpiration);
    return expiration.before(new Date());
  }
}
