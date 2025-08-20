package com.fema.tcc.gateways.postgresql.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_dietary_restriction")
public class UserDietaryRestrictionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "restriction_id")
  private DietaryRestrictionEntity dietaryRestriction;

  private String notes;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_at")
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
