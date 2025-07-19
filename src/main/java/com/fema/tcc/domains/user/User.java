package com.fema.tcc.domains.user;

import com.fema.tcc.domains.enums.UserRole;
import com.fema.tcc.domains.medicine.Medicine;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class User {
  private Integer userId;
  private String name;
  private String email;
  private String password;
  private Date createAt;
  private UserRole role;

  @Setter private String fcmToken;
  private List<Medicine> medicines = new ArrayList<>();

  public User(Integer userId) {
    this.userId = userId;
  }

  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public User(String name, String email, String password, UserRole role) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.createAt = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
    this.role = role;
    this.medicines = new ArrayList<>();
  }

  public User(
      Integer userId,
      String name,
      String email,
      String password,
      Date date,
      UserRole role,
      String fcmToken,
      List<Medicine> medicines) {
    this.userId = userId;
    this.name = name;
    this.email = email;
    this.password = password;
    this.createAt = date;
    this.role = role;
    this.fcmToken = fcmToken;
    this.medicines = medicines;
  }

  public User(String name, String email, String password, UserRole role, List<Medicine> medicines) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
    this.medicines = medicines;
  }
}
