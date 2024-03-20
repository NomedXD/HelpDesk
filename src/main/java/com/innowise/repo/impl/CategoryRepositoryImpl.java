package com.innowise.repo.impl;

import com.innowise.domain.Category;
import com.innowise.repo.CategoryRepository;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    @PersistenceContext
    private Session session;

    @Override
    public Optional<Category> findById(Integer id) {
        return Optional.ofNullable(session.find(Category.class, id));
    }

    @Override
    public List<Category> findAll() {
        return session.createQuery("From Category", Category.class).list();
    }

    @Override
    public Category save(Category category) {
        return session.merge(category);
    }

    @Override
    public Category update(Category category) {
        return session.merge(category);
    }

    @Override
    public void delete(Integer id) {
        Optional<Category> categoryToDelete = Optional.ofNullable(session.find(Category.class, id));
        if (categoryToDelete.isEmpty()) {
            //todo throw exception
        }

        session.remove(categoryToDelete);
    }
}
