package com.fema.tcc.gateways.http.mappers;

import com.fema.tcc.domains.user.User;
import com.fema.tcc.gateways.http.jsons.UserResponseJson;
import com.fema.tcc.gateways.postgresql.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MedicineJsonMapper.class})
public interface UserJsonMapper {

  UserEntity domainToEntity(User user);

  User entityToDomain(UserEntity entity);

  UserResponseJson domainToResponse(User user);
}
