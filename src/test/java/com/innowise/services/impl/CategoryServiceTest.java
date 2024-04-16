package com.innowise.services.impl;

import com.innowise.domain.Category;
import com.innowise.exceptions.NoSuchEntityIdException;
import com.innowise.repositories.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void findById_withValidCategoryId_returnsCategory() {
        Integer categoryId = 1;
        Category expectedCategory = Category.builder().id(categoryId).build();

        Mockito.when(categoryRepository.findById(Mockito.argThat(argument -> (argument != null) && (argument >= 1)))).
                thenReturn(Optional.of(expectedCategory));
        Category actualCategory = categoryService.findById(categoryId);

        Mockito.verify(categoryRepository, Mockito.times(1)).findById(categoryId);
        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(expectedCategory.getId(), actualCategory.getId());
    }

    @Test
    public void findById_withInvalidUserId_throwsNoSuchEntityIdException() {
        Integer invalidCategoryId = 999;

        Mockito.when(categoryRepository.findById(Mockito.argThat(argument -> (argument != null) && (argument >= 1)))).
                thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchEntityIdException.class, () -> categoryService.findById(invalidCategoryId));
        Mockito.verify(categoryRepository, Mockito.times(1)).findById(invalidCategoryId);
    }

    @Test
    public void findAll_withValid_returnsCategoryList() {
        String category1Name = "category1Name";
        String category2Name = "category2Name";
        List<Category> expectedCategoryList = List.of(
                Category.builder().name(category1Name).build(),
                Category.builder().name(category2Name).build());

        Mockito.when(categoryRepository.findAll()).thenReturn(expectedCategoryList);
        List<String> actualUserList = categoryService.findAll();

        Assertions.assertEquals(expectedCategoryList.stream().map(Category::getName).toList(), actualUserList);
        Mockito.verify(categoryRepository, Mockito.times(1)).findAll();
    }
}
