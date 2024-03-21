package com.innowise.exceptions;

public class NoSuchCommentException extends IllegalArgumentException {
    private final Integer commentId;

    public NoSuchCommentException(Integer commentId) {
        super(String.format("Comment with id %d is not found in DB", commentId));
        this.commentId = commentId;
    }
}
