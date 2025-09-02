package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.http.jsons.PrescriptionNotificationResponseJson;
import com.fema.tcc.gateways.http.jsons.PrescriptionUpdateNotificationRequestJson;
import com.fema.tcc.gateways.http.mappers.PrescriptionNotificationJsonMapper;
import com.fema.tcc.usecases.prescriptionNotification.FindUpcomingNotificationsUseCase;
import com.fema.tcc.usecases.prescriptionNotification.GetByIdPrescriptionNotificationUseCase;
import com.fema.tcc.usecases.prescriptionNotification.UpdateStatusUseCase;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prescriptions-notifications")
@AllArgsConstructor
public class PrescriptionNotificationController {

  private final UpdateStatusUseCase updateStatusUseCase;
  private final FindUpcomingNotificationsUseCase findUpcomingNotificationsUseCase;
  private final GetByIdPrescriptionNotificationUseCase getByIdPrescriptionNotificationUseCase;
  private final PrescriptionNotificationJsonMapper jsonMapper;

  @Operation(summary = "Alterar o status da notificação de uma prescrição")
  @PatchMapping("/update-status")
  @Transactional
  public ResponseEntity<PrescriptionNotificationResponseJson> changeNotificationStatus(
      @RequestBody PrescriptionUpdateNotificationRequestJson request) {

    PrescriptionNotification domain = jsonMapper.requestUpdateStatusToDomain(request);
    PrescriptionNotification responseDomain =
        updateStatusUseCase.execute(request.notificationId(), domain);
    PrescriptionNotificationResponseJson responseJson = jsonMapper.domainToResponse(responseDomain);

    return ResponseEntity.ok(responseJson);
  }

  @Operation(
      summary = "Buscar notificações de prescrições futuras do usuário atual",
      description =
          "Retorna uma lista de notificações de prescrições que ainda não foram marcadas como tomadas pelo usuário atual.")
  @GetMapping("/upcoming-notifications")
  public ResponseEntity<Page<PrescriptionNotificationResponseJson>> upcomingNotifications(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {

    Page<PrescriptionNotification> notifications =
        findUpcomingNotificationsUseCase.execute(page, size);

    Page<PrescriptionNotificationResponseJson> responseJsons =
        notifications.map(jsonMapper::domainToResponse);

    return ResponseEntity.ok().body(responseJsons);
  }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionNotificationResponseJson> getByID(@PathVariable Long id) {

        PrescriptionNotification notification = getByIdPrescriptionNotificationUseCase.execute(id);
        PrescriptionNotificationResponseJson responseJson = jsonMapper.domainToResponse(notification);

        return ResponseEntity.ok().body(responseJson);
    }
}
