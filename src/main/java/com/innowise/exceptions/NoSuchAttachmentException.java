package com.innowise.exceptions;

public class NoSuchAttachmentException extends IllegalArgumentException {
    private final Integer attachmentId;

    public NoSuchAttachmentException(Integer attachmentId) {
        super(String.format("Attachment with id %d is not found in DB", attachmentId));
        this.attachmentId = attachmentId;
    }
}
