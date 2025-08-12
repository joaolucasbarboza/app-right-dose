package com.fema.tcc.domains.dietaryRestriction;

import com.fema.tcc.domains.user.User;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDietaryRestriction {
  private Integer id;
  private User user;
  private DietaryRestriction dietaryRestriction;
  private String notes;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
