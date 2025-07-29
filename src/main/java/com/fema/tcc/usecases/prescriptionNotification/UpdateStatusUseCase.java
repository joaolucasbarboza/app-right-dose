package com.fema.tcc.usecases.prescriptionNotification;

import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.PrescriptionNotificationGateway;
import com.fema.tcc.usecases.user.UserUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class UpdateStatusUseCase {

    private final PrescriptionNotificationGateway notificationGateway;
    private final UserUseCase userUseCase;

    public PrescriptionNotification execute(Long prescriptionId, Long notificationId, PrescriptionNotification updateRequest) {
        Integer currentUserId = userUseCase.getCurrentUser();

        PrescriptionNotification notification = notificationGateway.findById(notificationId);

        Prescription prescription = notification.getPrescription();
        if (!prescription.getId().equals(prescriptionId)) {
            throw new IllegalArgumentException("A notificação não pertence à prescrição informada.");
        }

        if (!prescription.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("Usuário não autorizado para alterar essa notificação.");
        }

        if (notification.getStatus().equals(updateRequest.getStatus())) {
            log.info("O status da notificação já está definido como {}", updateRequest.getStatus());
            throw new IllegalArgumentException(
                    "O status da notificação já está definido como " + updateRequest.getStatus() + ".");
        }

        notification.setStatus(updateRequest.getStatus());
        notification.setUpdatedAt(LocalDateTime.now());

        return notificationGateway.save(notification);
    }
}
