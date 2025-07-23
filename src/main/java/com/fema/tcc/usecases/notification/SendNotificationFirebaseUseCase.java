package com.fema.tcc.usecases.notification;

import com.fema.tcc.gateways.StringConverterGateway;
import com.fema.tcc.gateways.http.jsons.NotificationPayloadJson;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SendNotificationFirebaseUseCase {

  private static final String NOTIFICATION_TITLE = "Lembrete de medicação";
  private final FirebaseMessaging firebaseMessaging;
  private final StringConverterGateway<NotificationPayloadJson> stringConverterGateway;
  private final UpdateStatusNotificationUseCase updateStatusNotificationUseCase;

  public void execute(String message) {
    try {
      NotificationPayloadJson payloadJson = stringConverterGateway.fromString(message);
      String notificationMessage = payloadJson.message();

      log.info("[JOB: SendNotificationFirebaseUseCase] - START - Sending notification");
      log.info("[JOB: SendNotificationFirebaseUseCase] - Payload: {}", payloadJson);
      Message firebaseMessage =
          Message.builder()
              .setToken(payloadJson.userToken())
              .setNotification(
                  Notification.builder()
                      .setTitle(NOTIFICATION_TITLE)
                      .setBody(notificationMessage)
                      .build())
              .build();

      String response = firebaseMessaging.send(firebaseMessage);
      log.info("Resposta do Firebase: {}", response);
      updateStatusNotificationUseCase.execute(payloadJson.notificationId());
      log.info("[JOB: SendNotificationFirebaseUseCase] - END - Notification sent successfully");
    } catch (Exception e) {
      throw new RuntimeException("Failed to send notification", e);
    }
  }
}
