package com.fema.tcc.usecases.user;

import com.fema.tcc.config.security.AuthService;
import com.fema.tcc.domains.user.User;
import com.fema.tcc.gateways.UserGateway;
import com.fema.tcc.gateways.http.jsons.FcmTokenRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserUseCase {

  private final UserGateway userGateway;
  private final AuthService authService;

  public User getUser() {
    return userGateway.findById(authService.getCurrentUserId());
  }

  public Integer getCurrentUser() {
    return authService.getCurrentUserId();
  }

  public User updateToken(FcmTokenRequest fcmTokenRequest) {
    User user = getUser();
    user.setFcmToken(fcmTokenRequest.fcmToken());
    return userGateway.update(user);
  }
}
