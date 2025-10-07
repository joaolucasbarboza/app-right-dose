package com.fema.tcc.useCases.prescriptionNotification;

import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.PrescriptionNotificationGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetByIdPrescriptionNotificationUseCase {

  private final PrescriptionNotificationGateway prescriptionNotificationGateway;

  public PrescriptionNotification execute(Long id) {
    return prescriptionNotificationGateway.findById(id);
  }
}
