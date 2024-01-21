package com.gtech.db.listener;

import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class UpdatedAtEntityListener {

    @PreUpdate
    public void preUpdate(Auditable entity) {
        entity.setUpdatedAt(LocalDateTime.now());
    }
}