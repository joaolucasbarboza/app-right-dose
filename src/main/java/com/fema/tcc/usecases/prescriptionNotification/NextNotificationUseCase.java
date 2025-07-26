package com.fema.tcc.usecases.prescriptionNotification;

import com.fema.tcc.domains.prescription.Prescription;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fema.tcc.utils.NotificationIntervalUtil.calculateInterval;

@Component
public class NextNotificationUseCase {

    public List<LocalDateTime> execute(Prescription prescription, int quantityGenerate) {
        List<LocalDateTime> times = new ArrayList<>();

        LocalDateTime current = LocalDateTime.now();
        Duration interval = calculateInterval(prescription);

        for (int i = 0; i < quantityGenerate; i++) {
            current = current.plus(interval);
            times.add(current);
        }

        return times;
    }
}
