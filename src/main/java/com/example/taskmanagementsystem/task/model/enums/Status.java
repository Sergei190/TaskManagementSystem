package com.example.taskmanagementsystem.task.model.enums;

import java.util.Optional;

public enum Status {

    IN_WAITING,
    IN_PROGRESS,
    COMPLETED;

    public static Optional<Status> from(String stringStatus) {
        for (Status status : values()) {
            if (status.name().equals(stringStatus)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
