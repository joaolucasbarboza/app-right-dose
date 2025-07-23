package com.fema.tcc.gateways.postgresql.entity;

import com.fema.tcc.domains.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@Table(name = "users")
public class UserEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @Column(unique = true)
  @NotNull
  private String email;

  @NotNull
  @Column(name = "password_hash")
  private String password;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at", updatable = false)
  private Date createdAt;

  @NotNull
  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Column(name = "fcm_token")
  private String fcmToken;

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
