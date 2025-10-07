package com.fema.tcc.usecases.auth;

import com.fema.tcc.config.security.TokenService;
import com.fema.tcc.domains.user.User;
import com.fema.tcc.gateways.postgresql.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthUseCase {

  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;

  @Autowired
  public AuthUseCase(AuthenticationManager authenticationManager, TokenService tokenService) {
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
  }

  public String execute(User user) {
    UsernamePasswordAuthenticationToken usernamePassword =
        new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

    Authentication authentication = this.authenticationManager.authenticate(usernamePassword);

    return tokenService.generateToken((UserEntity) authentication.getPrincipal());
  }
}
