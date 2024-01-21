package com.gtech.db.listener;

import java.time.LocalDateTime;

public interface Auditable {

    default void setUpdatedAt(LocalDateTime time) {
        // Do nothing
    }
}