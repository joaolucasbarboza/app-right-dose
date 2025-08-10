package com.fema.tcc.usecases.prescriptionNotification;

import com.fema.tcc.domains.prescriptionNotification.PrescriptionNotification;
import com.fema.tcc.gateways.PrescriptionNotificationGateway;
import com.fema.tcc.usecases.user.UserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindUpcomingNotificationsUseCase {

  private final PrescriptionNotificationGateway prescriptionNotificationGateway;
  private final UserUseCase userUseCase;

  public Page<PrescriptionNotification> execute(int page, int size) {
    Integer currentUserId = userUseCase.getCurrentUser();

    Pageable pageable = PageRequest.of(page, size, Sort.by("notificationTime").ascending());

    return prescriptionNotificationGateway.findAllByUserId(currentUserId, pageable);
  }
}
