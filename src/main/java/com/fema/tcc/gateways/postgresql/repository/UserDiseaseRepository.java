package com.fema.tcc.gateways.postgresql.repository;

import com.fema.tcc.gateways.postgresql.entity.UserDiseaseEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDiseaseRepository extends JpaRepository<UserDiseaseEntity, Integer> {
  List<UserDiseaseEntity> findAllByUser_Id(Integer userId);
}
