package com.fema.tcc.domains.enums;

import lombok.Getter;

@Getter
public enum MedicineUnit {
  TABLET,
  CAPSULE,
  SYRINGE,
  BOTTLE,
  PATCH,
  DROPS,
  ML, // útil para líquidos
  TEASPOON,
  DOSE,
  CUSTOM
}
