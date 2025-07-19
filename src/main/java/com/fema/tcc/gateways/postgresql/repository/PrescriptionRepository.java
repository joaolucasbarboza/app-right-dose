package com.fema.tcc.gateways.postgresql.repository;

import com.fema.tcc.gateways.postgresql.entity.PrescriptionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepository extends JpaRepository<PrescriptionEntity, Long> {
  List<PrescriptionEntity> findAllByUser_UserId(Integer userUserId);
}
