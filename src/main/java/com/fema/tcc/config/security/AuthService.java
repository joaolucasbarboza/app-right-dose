package com.fema.tcc.config.security;

import com.fema.tcc.gateways.postgresql.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  public UserEntity getCurrentUser() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();

    return (UserEntity) authentication.getPrincipal();
  }

  public Integer getCurrentUserId() {
    return getCurrentUser().getId();
  }
}
