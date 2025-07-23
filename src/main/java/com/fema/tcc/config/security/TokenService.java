package com.fema.tcc.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fema.tcc.gateways.postgresql.entity.UserEntity;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  @Value("${api.security.token.secret}")
  private String secretKey;

  public String generateToken(UserEntity userEntity) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secretKey);

      return JWT.create()
          .withIssuer("auth-right-dose")
          .withSubject(userEntity.getEmail())
          .withClaim("id", userEntity.getId())
          .withExpiresAt(genExpirationDate())
          .sign(algorithm);
    } catch (JWTCreationException exception) {
      throw new RuntimeException(exception.getMessage());
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secretKey);

      return JWT.require(algorithm)
          .withIssuer("auth-right-dose")
          .build()
          .verify(token)
          .getSubject();
    } catch (JWTVerificationException exception) {
      throw new RuntimeException(exception.getMessage());
    }
  }

  private Instant genExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }
}
