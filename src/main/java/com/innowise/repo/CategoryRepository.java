package com.innowise.repo;

import com.innowise.domain.Category;

import java.util.List;

public interface CategoryRepository {
    Category findById(Long id);
    List<Category> findAll();

    Category save(Category category);
    Category update(Category category);

    void delete(Long id);
}
