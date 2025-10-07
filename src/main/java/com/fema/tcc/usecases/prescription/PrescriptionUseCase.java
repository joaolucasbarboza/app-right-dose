package com.fema.tcc.usecases.prescription;

import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.PrescriptionGateway;
import com.fema.tcc.gateways.PrescriptionNotificationGateway;
import com.fema.tcc.gateways.http.exceptions.NotFoundException;
import com.fema.tcc.usecases.user.UserUseCase;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PrescriptionUseCase {

  private final PrescriptionGateway prescriptionGateway;
  private final PrescriptionNotificationGateway prescriptionNotificationGateway;
  private final UserUseCase userUseCase;

  public Prescription getById(Long id) {
    Integer currentUser = userUseCase.getCurrentUser();

    Prescription prescription =
        prescriptionGateway
            .findById(id)
            .filter(pres -> pres.getUser().getId().equals(currentUser))
            .orElseThrow(() -> new NotFoundException("Prescription not found for user"));

    List<PrescriptionNotification> notifications =
        prescriptionNotificationGateway.findAllByPrescriptionId(prescription.getId());

    prescription.setNotifications(notifications);
    return prescription;
  }

  public void deleteById(Long id) {

    prescriptionGateway
        .findById(id)
        .ifPresentOrElse(
            prescription -> prescriptionGateway.deleteById(id),
            () -> {
              throw new NotFoundException("Prescription not found");
            });
  }
}
