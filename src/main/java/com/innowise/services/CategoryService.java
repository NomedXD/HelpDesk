package com.innowise.services;

import com.innowise.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    String findById(Integer id);

    List<String> findAll();

    Optional<Category> findByIdService(Integer id);
}
