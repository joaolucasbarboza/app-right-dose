package com.fema.tcc.usecases.prescription;

import com.fema.tcc.domains.medicine.Medicine;
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

  public Prescription execute(Prescription request, Integer medicineId) {
    User user = userUseCase.getUser();

    return medicineGateway
        .findById(medicineId)
        .map(
            medicine -> {
              if (!medicine.getUser().getId().equals(user.getId())) {
                throw new SecurityException("User is not authorized to perform this action");
              }

                LocalDateTime endDate = null;

              if (!request.isIndefinite()) {
                  if (request.getTotalOccurrences() == null) {
                    throw new IllegalArgumentException(
                        "Total occurrences must be specified for non-indefinite prescriptions");
                  } else {
                    endDate = calculateEndDate.execute(request);
                  }
              }

              Prescription prescription = buildPrescription(request, medicine, user, endDate);

              Prescription prescriptionSaved = prescriptionGateway.save(prescription);

              if (request.isWantsNotifications()) {
                generateNotificationsFlow.execute(prescriptionSaved, 2);
              }

              return prescriptionSaved;
            })
        .orElseThrow(() -> new NotFoundException("Medicine not found"));
  }

  private Prescription buildPrescription(Prescription request, Medicine medicine, User user, LocalDateTime endDate) {
    return Prescription.builder()
        .medicine(medicine)
        .user(user)
        .dosageAmount(request.getDosageAmount())
        .dosageUnit(request.getDosageUnit())
        .frequency(request.getFrequency())
        .uomFrequency(request.getUomFrequency())
        .indefinite(request.isIndefinite())
        .totalOccurrences(request.getTotalOccurrences())
        .startDate(request.getStartDate())
        .endDate(endDate)
        .wantsNotifications(request.isWantsNotifications())
        .instructions(request.getInstructions())
        .createdAt(LocalDateTime.now())
        .build();
  }
}
