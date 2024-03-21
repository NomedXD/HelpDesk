package com.innowise.services.impl;

import com.innowise.domain.Category;
import com.innowise.exceptions.NoSuchCategoryException;
import com.innowise.repositories.CategoryRepository;
import com.innowise.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category findById(Integer id) throws NoSuchCategoryException{
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchCategoryException(id));
    }

    @Override
    public List<String> findAll() {
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .toList();
    }
}
