package com.fema.tcc.domains.user;

import com.fema.tcc.domains.enums.UserRole;
import java.util.Date;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
  private Integer id;
  private String name;
  private String email;
  private String password;
  private Date createdAt;
  private UserRole role;

  private String fcmToken;
}
