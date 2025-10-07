package com.fema.tcc.usecases.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.fema.tcc.domains.medicine.Medicine;
import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.domains.user.User;
import com.fema.tcc.gateways.NotificationGateway;
import com.fema.tcc.gateways.PrescriptionNotificationGateway;
import com.fema.tcc.gateways.http.jsons.NotificationPayloadJson;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationUseCaseTest {

    @Mock private NotificationGateway notificationGateway;

    @Mock private PrescriptionNotificationGateway prescriptionNotificationGateway;

    @InjectMocks private NotificationUseCase notificationUseCase;

    @Test
    void shouldFetchNotificationsWithinTenMinuteWindow() {
        when(prescriptionNotificationGateway.findAllReadyToNotify(any(), any()))
                .thenReturn(Collections.emptyList());

        notificationUseCase.execute();

        ArgumentCaptor<LocalDateTime> nowCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> limitCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        verify(prescriptionNotificationGateway)
                .findAllReadyToNotify(nowCaptor.capture(), limitCaptor.capture());

        Duration window = Duration.between(nowCaptor.getValue(), limitCaptor.getValue());
        assertEquals(10, window.toMinutes());
        assertEquals(600, window.toSeconds());
        verifyNoInteractions(notificationGateway);
    }

    @Test
    void shouldSkipWhenUserDisabledNotifications() {
        PrescriptionNotification notification =
                buildNotification(false, null);

        when(prescriptionNotificationGateway.findAllReadyToNotify(any(), any()))
                .thenReturn(List.of(notification));

        notificationUseCase.execute();

        verify(notificationGateway, never()).publish(any(NotificationPayloadJson.class));
    }

    @Test
    void shouldThrowWhenFcmTokenMissing() {
        PrescriptionNotification notification =
                buildNotification(true, null);

        when(prescriptionNotificationGateway.findAllReadyToNotify(any(), any()))
                .thenReturn(List.of(notification));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> notificationUseCase.execute());

        assertEquals("No FCM token found for notification ID: 1", exception.getMessage());
        verify(notificationGateway, never()).publish(any(NotificationPayloadJson.class));
    }

    @Test
    void shouldPublishNotificationWhenDataIsValid() {
        PrescriptionNotification notification =
                buildNotification(true, "token-123");

        when(prescriptionNotificationGateway.findAllReadyToNotify(any(), any()))
                .thenReturn(List.of(notification));

        notificationUseCase.execute();

        NotificationPayloadJson expectedPayload =
                new NotificationPayloadJson(
                        1L,
                        "token-123",
                        "Está na hora de tomar sua medicação: Dipirona");

        verify(notificationGateway).publish(expectedPayload);
    }

    private PrescriptionNotification buildNotification(
            boolean wantsNotifications, String fcmToken) {
        User user = User.builder().fcmToken(fcmToken).build();

        Medicine medicine = Medicine.builder().name("Dipirona").user(user).build();

        Prescription prescription =
                Prescription.builder()
                        .id(1L)
                        .medicine(medicine)
                        .wantsNotifications(wantsNotifications)
                        .build();

        return PrescriptionNotification.builder().id(1L).prescription(prescription).build();
    }
}