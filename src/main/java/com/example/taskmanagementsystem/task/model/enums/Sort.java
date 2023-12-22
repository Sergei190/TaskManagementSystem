package com.example.taskmanagementsystem.task.model.enums;

import java.util.Optional;

public enum Sort {

    PRIORITY,
    STATUS;

    public static Optional<Sort> from(String stringSort) {
        for (Sort sort : values()) {
            if (sort.name().equals(stringSort)) {
                return Optional.of(sort);
            }
        }
        return Optional.empty();
    }

}