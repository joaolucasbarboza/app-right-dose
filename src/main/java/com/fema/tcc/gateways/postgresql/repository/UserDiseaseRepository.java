package com.fema.tcc.gateways.postgresql.repository;

import com.fema.tcc.gateways.postgresql.entity.UserDiseaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDiseaseRepository extends JpaRepository<UserDiseaseEntity, Integer> {
    List<UserDiseaseEntity> findAllByUser_Id(Integer userId);
}
