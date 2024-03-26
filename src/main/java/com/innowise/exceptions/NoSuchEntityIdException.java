package com.innowise.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NoSuchEntityIdException extends IllegalArgumentException {
    private final Integer entityId;

    public NoSuchEntityIdException(EntityTypeMessages entityType, Integer entityId) {
        super(String.format(entityType.getMessage(), entityId));
        this.entityId = entityId;
    }
}
