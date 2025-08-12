package com.fema.tcc.domains.dietaryRestriction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DietaryRestriction {
  private Integer id;
  private String code;
  private String description;
}
