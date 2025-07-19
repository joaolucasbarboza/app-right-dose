package com.fema.tcc.jobs;

import com.fema.tcc.usecases.notification.NotificationUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class NotificationJob {

  private final NotificationUseCase notificationUseCase;

  @Scheduled(cron = "${schedule.notification-job}")
  public void execute() {
    log.info("[JOB: NotificationJob] - START - Executando job de notificação");
    try {
      notificationUseCase.execute();
      log.info("[JOB: NotificationJob] - END - Job de notificação executado com sucesso");
    } catch (Exception e) {
      log.error("[JOB: NotificationJob] - ERROR - Erro ao executar job", e);
    }
  }
}
