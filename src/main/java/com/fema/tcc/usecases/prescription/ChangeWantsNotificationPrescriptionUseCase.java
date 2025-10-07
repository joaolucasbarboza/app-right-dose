package com.fema.tcc.usecases.prescription;

import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.gateways.PrescriptionGateway;
import com.fema.tcc.usecases.user.UserUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ChangeWantsNotificationPrescriptionUseCase {

    private final PrescriptionGateway prescriptionGateway;
    private final UserUseCase userUseCase;

    public void execute(Long prescriptionId) {
        log.info("Changing wantsNotification for prescription with id: {}", prescriptionId);

        Integer currentUserId = userUseCase.getUser().getId();
        try {
            Prescription prescription = prescriptionGateway.findById(prescriptionId)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Prescription not found with id: " + prescriptionId));

            if (!prescription.getMedicine().getUser().getId().equals(currentUserId)) {
                log.info("User not authorized to change this prescription with id: {}", prescriptionId);
                throw new IllegalArgumentException("User not authorized to change this prescription.");
            }

            prescription.setWantsNotifications(!prescription.isWantsNotifications());
            prescriptionGateway.save(prescription);

            log.info("Successfully changed wantsNotification for prescription with id: {}", prescriptionId);
        } catch (RuntimeException e) {
            log.error("Error changing wantsNotification for prescription with id: {}", prescriptionId);
        }
    }
}
