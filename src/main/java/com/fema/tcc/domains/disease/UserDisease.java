package com.fema.tcc.domains.disease;

import com.fema.tcc.domains.user.User;
import java.time.LocalDateTime;
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
public class UserDisease {
  private Integer id;
  private User user;
  private Disease disease;
  private String notes;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
