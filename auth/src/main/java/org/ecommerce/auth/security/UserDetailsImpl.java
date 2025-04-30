package org.ecommerce.auth.security;

import org.ecommerce.auth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
  private final String id;
  private final String username;
  private final String password;
  private final List<String> roles;

  public static UserDetailsImpl build(User user) {
    return new UserDetailsImpl(
        user.getId().toString(),
        user.getUsername(),
        user.getPassword(),
        user.getRoles());
  }

  public String getId() {
    return id;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

}
