package com.innowise.services.impl;

import com.innowise.domain.Category;
import com.innowise.exceptions.EntityTypeMessages;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.repositories.CategoryRepository;
import com.innowise.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityIdException(EntityTypeMessages.CATEGORY_MESSAGE, id));
    }

    @Override
    public List<String> findAll() {
        return categoryRepository.findAll().stream()
                .map(Category::getName)
                .toList();
    }

    /*@Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }*/
}
