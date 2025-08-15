package com.fema.tcc.gateways.postgresql.repository;

import com.fema.tcc.gateways.postgresql.entity.UserDietaryRestrictionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDietaryRestrictionRepository
    extends JpaRepository<UserDietaryRestrictionEntity, Integer> {
  List<UserDietaryRestrictionEntity> findAllByUser_Id(Integer userId);
}
