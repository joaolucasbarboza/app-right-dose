package com.fema.tcc.useCases.prescription;

import com.fema.tcc.domains.medicine.Medicine;
import com.fema.tcc.domains.prescription.Prescription;
import com.fema.tcc.domains.user.User;
import com.fema.tcc.gateways.MedicineGateway;
import com.fema.tcc.gateways.PrescriptionGateway;
import com.fema.tcc.gateways.http.exceptions.NotFoundException;
import com.fema.tcc.useCases.user.UserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdatePrescriptionUseCase {

  private final UserUseCase userUseCase;
  private final PrescriptionGateway prescriptionGateway;
  private final MedicineGateway medicineGateway;

  public Prescription execute(Long id, Prescription updatedPrescription) {
    User user = userUseCase.getUser();

    return prescriptionGateway
        .findById(id)
        .map(
            prescription -> {
              if (!prescription.getUser().getId().equals(user.getId())) {
                throw new SecurityException("User is not authorized to perform this action");
              }

              if (prescription
                  .getMedicine()
                  .getId()
                  .equals(updatedPrescription.getMedicine().getId())) {
                throw new SecurityException("Medicine already registered");
              }

              Medicine medicine =
                  medicineGateway
                      .findById(updatedPrescription.getMedicine().getId())
                      .map(
                          foundMedicine -> {
                            if (!foundMedicine.getUser().getId().equals(user.getId())) {
                              throw new SecurityException(
                                  "User is not authorized to perform this action");
                            }
                            return foundMedicine;
                          })
                      .orElseThrow(() -> new NotFoundException("Medicine not found"));

              prescription.setMedicine(medicine);
              prescription.updateFrom(updatedPrescription);
              return prescription;
            })
        .map(prescriptionGateway::save)
        .orElseThrow(() -> new NotFoundException("Prescription not found"));
  }
}
