package com.fema.tcc.usecases.prescriptionNotification;

import com.fema.tcc.gateways.PrescriptionNotificationGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteNotificationUseCase {

  private final PrescriptionNotificationGateway gateway;

  public void execute(Long notificationId) {
    gateway.deleteById(notificationId);
  }
}
