package com.innowise.exceptions;

public class NoSuchCategoryException extends IllegalArgumentException {
    private final Integer categoryId;

    public NoSuchCategoryException(Integer categoryId) {
        super(String.format("Category with id %d is not found in DB", categoryId));
        this.categoryId = categoryId;
    }
}
