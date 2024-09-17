package com.back_end.myProject.service.impl;

import com.back_end.myProject.dto.CategoryDTO;
import com.back_end.myProject.dto.UserDTO;
import com.back_end.myProject.entities.Category;
import com.back_end.myProject.entities.User;
import com.back_end.myProject.repositorys.CategoryRepository;
import com.back_end.myProject.service.ICategory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements ICategory {
    @Autowired
    private ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean createCategory(CategoryDTO categoryDTO) {
       // find category
        Optional<Category> category = categoryRepository.findByName(categoryDTO.getName());
        if (category.isPresent()) {
            return false;
        }
        Category newCategory = new Category();
        newCategory.setName(categoryDTO.getName());
        newCategory.setDescription(categoryDTO.getDescription());
        categoryRepository.save(newCategory);
        return true;
    }

    @Override
    public boolean updateCategory(CategoryDTO categoryDTO) {
        System.out.println(categoryDTO.getName());
        // find category
        Optional<Category> category = categoryRepository.findById(categoryDTO.getId());
        if (category.isPresent()) {
            category.get().setName(categoryDTO.getName());
            category.get().setDescription(categoryDTO.getDescription());
            categoryRepository.save(category.get());
            return true;
        }
        return false;

    }

    @Override
    public boolean deleteCategory(Long id) {
        // find byID
        Category category = categoryRepository.findById(id).get();
        if (category == null) {
            return false;
        }
        categoryRepository.delete(category);
        return true;
    }

    @Override
    public Page<Category> getCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(user -> modelMapper.map(user, Category.class));
    }


}
