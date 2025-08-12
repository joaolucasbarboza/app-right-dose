package com.fema.tcc.domains.disease;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Disease {
  private Integer id;
  private String code;
  private String description;
}
