package com.fema.tcc.gateways;

import com.fema.tcc.domains.user.User;

public interface UserGateway {

  User findById(Integer userId);

  User save(User user);

  User update(User user);

  boolean existsByEmail(String email);
}
