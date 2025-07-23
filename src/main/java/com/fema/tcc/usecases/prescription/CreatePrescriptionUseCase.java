package com.fema.tcc.usecases.prescription;

import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.domains.user.User;
import com.fema.tcc.gateways.MedicineGateway;
import com.fema.tcc.gateways.PrescriptionGateway;
import com.fema.tcc.gateways.http.exceptions.NotFoundException;
import com.fema.tcc.usecases.prescriptionNotification.CalculateEndDate;
import com.fema.tcc.usecases.prescriptionNotification.GenerateNotificationsFlow;
import com.fema.tcc.usecases.user.UserUseCase;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreatePrescriptionUseCase {

  private final GenerateNotificationsFlow generateNotificationsFlow;
  private final UserUseCase userUseCase;
  private final CalculateEndDate calculateEndDate;
  private final MedicineGateway medicineGateway;
  private final PrescriptionGateway prescriptionGateway;

  public Prescription execute(Prescription request) {
    User user = userUseCase.getUser();

    return medicineGateway
        .findById(request.getMedicine().getId())
        .map(
            medicine -> {
              if (!medicine.getUser().getId().equals(user.getId())) {
                throw new SecurityException("User is not authorized to perform this action");
              }

              LocalDateTime endDate = calculateEndDate.execute(request);

              Prescription prescription =
                  Prescription.builder()
                      .medicine(medicine)
                      .user(medicine.getUser())
                      .dosageAmount(request.getDosageAmount())
                      .dosageUnit(request.getDosageUnit())
                      .frequency(request.getFrequency())
                      .uomFrequency(request.getUomFrequency())
                      .totalDays(request.getTotalDays())
                      .startDate(request.getStartDate())
                      .endDate(endDate)
                      .wantsNotifications(request.isWantsNotifications())
                      .instructions(request.getInstructions())
                      .createdAt(LocalDateTime.now())
                      .build();

              Prescription prescriptionSaved = prescriptionGateway.save(prescription);

              if (request.isWantsNotifications()) {
                generateNotificationsFlow.execute(prescriptionSaved);
              }

              return prescriptionSaved;
            })
        .orElseThrow(() -> new NotFoundException("Medicine not found"));
  }
}
