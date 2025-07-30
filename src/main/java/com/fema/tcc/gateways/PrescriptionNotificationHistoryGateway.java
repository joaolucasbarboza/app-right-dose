package com.fema.tcc.gateways;

import com.fema.tcc.domains.prescriptionNotificationHistory.PrescriptionNotificationHistory;

public interface PrescriptionNotificationHistoryGateway {

    void save(PrescriptionNotificationHistory prescriptionNotificationHistory);
}
