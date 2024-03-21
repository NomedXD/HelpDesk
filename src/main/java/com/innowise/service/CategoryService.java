package com.innowise.service;

import java.util.List;

public interface CategoryService {
    String findById(Integer id);
    List<String> findAll();
}
