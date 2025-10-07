package com.fema.tcc.usecases.auth;

import com.fema.tcc.domains.user.User;
import com.fema.tcc.gateways.UserGateway;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUseCase {

  private final UserGateway userGateway;

  public RegisterUseCase(UserGateway userGateway) {
    this.userGateway = userGateway;
  }

  public User execute(User user) {

    if (userGateway.existsByEmail(user.getEmail())) {
      throw new RuntimeException("User already exists");
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());

    User newUser =
        User.builder()
            .name(user.getName())
            .email(user.getEmail())
            .password(encryptedPassword)
            .role(user.getRole())
            .createdAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
            .build();

    return userGateway.save(newUser);
  }
}
