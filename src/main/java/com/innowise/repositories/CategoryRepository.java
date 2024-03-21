package com.innowise.repositories;

import com.innowise.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);

    List<Category> findAll();

    Category update(Category category);

    void delete(Integer id);

    boolean existsById(Integer id);

    Optional<Category> findById(Integer id);
}
