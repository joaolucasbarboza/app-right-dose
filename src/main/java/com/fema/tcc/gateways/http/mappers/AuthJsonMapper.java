package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.user.User;
import com.fema.tcc.gateways.http.jsons.LoginRequestJson;
import com.fema.tcc.gateways.http.jsons.LoginResponseJson;
import com.fema.tcc.gateways.http.jsons.RegisterRequestJson;
import com.fema.tcc.gateways.http.jsons.RegisterResponseJson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthJsonMapper {

  User loginRequestToDomain(LoginRequestJson loginRequestJson);

  LoginResponseJson toLoginResponseJson(String token);

  User registerRequestToDomain(RegisterRequestJson registerRequestJson);

  RegisterResponseJson domainToRegisterResponse(User user);
}
