package com.fema.tcc.usecases.medicine;

import com.fema.tcc.domains.medicine.Medicine;
import com.fema.tcc.domains.user.User;
import com.fema.tcc.gateways.MedicineGateway;
import com.fema.tcc.gateways.http.exceptions.NotFoundException;
import com.fema.tcc.usecases.user.UserUseCase;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MedicineUseCase {

  private final UserUseCase userUseCase;
  private final MedicineGateway medicineGateway;

  public Medicine create(Medicine medicine) {

    User user = userUseCase.getUser();
    medicine.setUser(user);
    return medicineGateway.save(medicine);
  }

  public List<Medicine> getMedicines() {
    User user = userUseCase.getUser();
    return medicineGateway.findMedicinesByUserId(user);
  }

  public Medicine getMedicineById(Integer id) {
    return medicineGateway
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Medicine not found"));
  }

  public Medicine updateMedicine(Medicine medicineUpdate) {
    User user = userUseCase.getUser();

    Medicine medicine =
        medicineGateway
            .findById(medicineUpdate.getId())
            .orElseThrow(() -> new NotFoundException("Medicine not found"));

    if (!medicine.getUser().getId().equals(user.getId())) {
      throw new SecurityException("You do not have permission to update this medicine");
    }

    medicine.setName(medicineUpdate.getName());
    medicine.setDescription(medicineUpdate.getDescription());

    return medicineGateway.save(medicine);
  }

  public void deleteMedicine(Integer id) {
    medicineGateway
        .findById(id)
        .ifPresentOrElse(
            medicine -> {
              if (!medicine.getUser().getId().equals(userUseCase.getUser().getId())) {
                throw new SecurityException("You do not have permission to delete this medicine");
              }
              medicineGateway.deleteMedicine(medicine);
            },
            () -> {
              throw new NotFoundException("Medicine not found");
            });
  }
}
