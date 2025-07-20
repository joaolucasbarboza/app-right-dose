package com.fema.tcc.domains.medicine;

import com.fema.tcc.domains.user.User;
import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Medicine {
  private Integer id;
  private String name;
  private String description;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private User user;

  public Medicine(
      String name,
      String description) {
    this.name = name;
    this.description = description;
  }
}
