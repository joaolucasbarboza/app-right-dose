package com.fema.tcc.gateways.postgresql.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "disease")
public class DiseaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "disease_id")
  private Integer id;

  @Column(unique = true)
  private String code;

  private String description;
}
