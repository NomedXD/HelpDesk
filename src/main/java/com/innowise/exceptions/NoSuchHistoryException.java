package com.innowise.exceptions;

public class NoSuchHistoryException extends IllegalArgumentException {
    private final Integer historyId;

    public NoSuchHistoryException(Integer historyId) {
        super(String.format("History with id %d is not found in DB", historyId));
        this.historyId = historyId;
    }
}
