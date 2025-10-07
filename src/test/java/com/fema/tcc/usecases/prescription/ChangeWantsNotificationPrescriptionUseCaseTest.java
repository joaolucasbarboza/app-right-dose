package com.fema.tcc.usecases.prescription;

import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.gateways.PrescriptionGateway;
import com.fema.tcc.usecases.user.UserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;

@ExtendWith(MockitoExtension.class)
class ChangeWantsNotificationPrescriptionUseCaseTest {

    @Mock
    private PrescriptionGateway prescriptionGateway;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private UserUseCase userUseCase;

    @InjectMocks
    private ChangeWantsNotificationPrescriptionUseCase useCase;

    @Test
    void shouldToggleFromTrueToFalseAndSave_whenOwnedByCurrentUser() {
        long prescriptionId = 10L;
        int currentUserId = 123;

        when(userUseCase.getUser().getId()).thenReturn(currentUserId);

        Prescription prescription = mock(Prescription.class, RETURNS_DEEP_STUBS);
        when(prescription.getMedicine().getUser().getId()).thenReturn(currentUserId);
        when(prescription.isWantsNotifications()).thenReturn(true);
        when(prescriptionGateway.findById(prescriptionId)).thenReturn(Optional.of(prescription));

        useCase.execute(prescriptionId);

        verify(prescription, times(1)).setWantsNotifications(false);
        verify(prescriptionGateway, times(1)).save(prescription);
    }

    @Test
    void shouldToggleFromFalseToTrueAndSave_whenOwnedByCurrentUser() {
        long prescriptionId = 11L;
        int currentUserId = 456;

        when(userUseCase.getUser().getId()).thenReturn(currentUserId);

        Prescription prescription = mock(Prescription.class, RETURNS_DEEP_STUBS);
        when(prescription.getMedicine().getUser().getId()).thenReturn(currentUserId);
        when(prescription.isWantsNotifications()).thenReturn(false);
        when(prescriptionGateway.findById(prescriptionId)).thenReturn(Optional.of(prescription));

        useCase.execute(prescriptionId);

        verify(prescription, times(1)).setWantsNotifications(true);
        verify(prescriptionGateway, times(1)).save(prescription);
    }

    @Test
    void shouldNotSave_whenPrescriptionNotFound_andShouldNotThrow() {
        long prescriptionId = 99L;
        when(userUseCase.getUser().getId()).thenReturn(1);
        when(prescriptionGateway.findById(prescriptionId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> useCase.execute(prescriptionId));

        verify(prescriptionGateway, never()).save(any());
    }

    @Test
    void shouldNotSave_whenUserNotAuthorized_andShouldNotThrow() {
        long prescriptionId = 12L;
        int currentUserId = 100;
        int ownerId = 200;

        when(userUseCase.getUser().getId()).thenReturn(currentUserId);

        Prescription prescription = mock(Prescription.class, RETURNS_DEEP_STUBS);
        when(prescription.getMedicine().getUser().getId()).thenReturn(ownerId);
        when(prescriptionGateway.findById(prescriptionId)).thenReturn(Optional.of(prescription));

        assertDoesNotThrow(() -> useCase.execute(prescriptionId));

        verify(prescription, never()).setWantsNotifications(anyBoolean());
        verify(prescriptionGateway, never()).save(any());
    }

    @Test
    void shouldCatchAndNotPropagate_whenSaveThrows() {
        long prescriptionId = 13L;
        int currentUserId = 777;

        when(userUseCase.getUser().getId()).thenReturn(currentUserId);

        Prescription prescription = mock(Prescription.class, RETURNS_DEEP_STUBS);
        when(prescription.getMedicine().getUser().getId()).thenReturn(currentUserId);
        when(prescription.isWantsNotifications()).thenReturn(true);
        when(prescriptionGateway.findById(prescriptionId)).thenReturn(Optional.of(prescription));

        doThrow(new RuntimeException("DB down")).when(prescriptionGateway).save(prescription);

        assertDoesNotThrow(() -> useCase.execute(prescriptionId));

        verify(prescription, times(1)).setWantsNotifications(false);
        verify(prescriptionGateway, times(1)).save(prescription);
    }
}
