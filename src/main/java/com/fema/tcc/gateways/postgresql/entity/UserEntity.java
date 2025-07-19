package com.fema.tcc.gateways.postgresql.entity;

import com.fema.tcc.domains.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@Table(name = "users")
public class UserEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Integer userId;

  private String name;

  @Column(unique = true)
  @NotNull
  private String email;

  @NotNull
  @Column(name = "password_hash")
  private String password;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at", updatable = false)
  private Date createdAt = new Date();

  @NotNull
  @Enumerated(EnumType.STRING)
  private UserRole role;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<MedicineEntity> medicines;

  @Column(name = "fcm_token")
  private String fcmToken;

  public UserEntity(
      Integer userId,
      String name,
      String email,
      String password,
      Date createdAt,
      UserRole role,
      String fcmToken) {
    this.userId = userId;
    this.name = name;
    this.email = email;
    this.password = password;
    this.createdAt = createdAt;
    this.role = role;
    this.fcmToken = fcmToken;
  }

  public UserEntity(Integer userId) {
    this.userId = userId;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (role == UserRole.ADMIN) {
      return List.of(
          new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
    } else {
      return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return UserDetails.super.isEnabled();
  }
}
