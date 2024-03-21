package com.innowise.services;

import com.innowise.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category findById(Integer id);

    List<String> findAll();
}
