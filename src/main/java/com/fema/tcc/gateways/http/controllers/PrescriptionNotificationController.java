package com.fema.tcc.gateways.http.controllers;

import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.http.jsons.PrescriptionNotificationResponseJson;
import com.fema.tcc.gateways.http.jsons.PrescriptionUpdateNotificationRequestJson;
import com.fema.tcc.gateways.http.mappers.PrescriptionNotificationJsonMapper;
import com.fema.tcc.usecases.prescriptionNotification.UpdateStatusUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("prescriptions-notifications")
@AllArgsConstructor
public class PrescriptionNotificationController {

    private final UpdateStatusUseCase updateStatusUseCase;
    private final PrescriptionNotificationJsonMapper jsonMapper;

    @Operation(summary = "Alterar o status da notificação de uma prescrição")
    @PatchMapping("/updateStatus")
    public ResponseEntity<PrescriptionNotificationResponseJson> changeNotificationStatus(
            @RequestBody PrescriptionUpdateNotificationRequestJson request) {

        PrescriptionNotification domain = jsonMapper.requestUpdateStatusToDomain(request);
        PrescriptionNotification responseDomain =
                updateStatusUseCase.execute(request.prescriptionId(), request.notificationId(), domain);
        PrescriptionNotificationResponseJson responseJson = jsonMapper.domainToResponse(responseDomain);

        return ResponseEntity.ok(responseJson);
    }
}
