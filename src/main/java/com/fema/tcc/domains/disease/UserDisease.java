package com.fema.tcc.domains.disease;

import com.fema.tcc.domains.user.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDisease {
  private Integer id;
  private User user;
  private Disease disease;
  private LocalDate diagnosedAt;
  private String notes;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
