package com.innowise.services.impl;

import com.innowise.domain.Category;
import com.innowise.exceptions.NoSuchCategoryException;
import com.innowise.repositories.CategoryRepository;
import com.innowise.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String findById(Integer id) throws NoSuchCategoryException{
        return categoryRepository.findById(id).orElseThrow(() -> new NoSuchCategoryException(id)).getName();
    }

    @Override
    public List<String> findAll() {
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .toList();
    }

    @Override
    public Optional<Category> findByIdService(Integer id) {
        return categoryRepository.findById(id);
    }
}
