package com.innowise.service.impl;

import com.innowise.domain.Category;
import com.innowise.repo.CategoryRepository;
import com.innowise.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String findById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(
                //todo throw Exception Not Found
        ).getName();
    }

    @Override
    public List<String> findAll() {
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .toList();
    }
}
