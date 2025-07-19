package com.fema.tcc.usecases.notification;

import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.NotificationGateway;
import com.fema.tcc.gateways.PrescriptionNotificationGateway;
import com.fema.tcc.gateways.http.jsons.NotificationPayloadJson;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class NotificationUseCase {

  private static final Integer LIMIT_TIME = 10;

  private final NotificationGateway notificationGateway;
  private final PrescriptionNotificationGateway prescriptionNotificationGateway;

  public void execute() {
    LocalDateTime currentDateTime = LocalDateTime.now();
    LocalDateTime limitTime = currentDateTime.plusMinutes(LIMIT_TIME);

    List<PrescriptionNotification> notifications =
        prescriptionNotificationGateway.findAllReadyToNotify(currentDateTime, limitTime);

    if (notifications.isEmpty()) {
      log.info("[JOB: NotificationJob] - FINISH PROCESS - No notifications found to process.");
      return;
    }

    notifications.forEach(
        notification -> {
          log.info(
              "[JOB: NotificationJob] - PROCESSING - Processing notification: {}", notification);

          Long notificationId = notification.getId();
          String fcmToken = notification.getPrescription().getMedicine().getUser().getFcmToken();
          String medicineName = notification.getPrescription().getMedicine().getName();
          String messageToSend =
              String.format("Está na hora de tomar sua medicação: %s", medicineName);

          NotificationPayloadJson payloadJson =
              new NotificationPayloadJson(notificationId, fcmToken, messageToSend);

          notificationGateway.publish(payloadJson);
          log.info("[JOB: NotificationJob] - PUBLISH - Notification sent successfully");
        });
  }
}
