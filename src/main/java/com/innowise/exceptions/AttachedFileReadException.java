package com.innowise.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AttachedFileReadException extends IllegalArgumentException {
    private final String fileName;

    public AttachedFileReadException(String fileName, String invokeErrorMessage) {
        super(String.format("Error while reading file %s. Inner level error message: %s", fileName, invokeErrorMessage));
        this.fileName = fileName;
    }
}
