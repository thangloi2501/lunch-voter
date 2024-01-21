package com.gtech.db.entity;

import com.gtech.db.listener.Auditable;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEntity implements Serializable {

  private static final long serialVersionUID = -5505173567335846034L;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected Long id;

  @PreUpdate
  public void preUpdate() {
    if (this instanceof Auditable auditable) {
      auditable.setUpdatedAt(LocalDateTime.now());
    }
  }
}
