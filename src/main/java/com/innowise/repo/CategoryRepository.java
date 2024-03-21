package com.innowise.repo;

import com.innowise.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findById(Integer id);
    List<Category> findAll();

    Category save(Category category);
    Category update(Category category);

    void delete(Integer id);
}
