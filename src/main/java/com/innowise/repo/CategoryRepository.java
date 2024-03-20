package com.innowise.repo;

import com.innowise.domain.Category;

import java.util.List;

public interface CategoryRepository {
    Category findById(Integer id);
    List<Category> findAll();

    Category save(Category category);
    Category update(Category category);

    void delete(Integer id);
}
