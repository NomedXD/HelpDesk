package com.innowise.repositories.impl;

import com.innowise.domain.Category;
import com.innowise.repositories.CategoryRepository;
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
    public Category save(Category category) {
        return session.merge(category);
    }

    @Override
    public List<Category> findAll() {
        return session.createQuery("FROM Category", Category.class).list();
    }

    @Override
    public Category update(Category category) {
        return session.merge(category);
    }

    @Override
    public void delete(Integer id) {
        session.createMutationQuery("DELETE FROM Category WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public boolean existsById(Integer id) {
        return session.createNativeQuery("SELECT COUNT(*) FROM categories WHERE id = :id", Integer.class)
                .setParameter("id", id)
                .getSingleResult() == 1;
    }

    @Override
    public Optional<Category> findById(Integer id) {
        return Optional.ofNullable(session.find(Category.class, id));
    }
}
