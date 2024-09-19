package com.back_end.myProject.service;

import com.back_end.myProject.dto.CategoryDTO;
import com.back_end.myProject.dto.UserDTO;
import com.back_end.myProject.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategory {
    boolean createCategory(CategoryDTO categoryDTO);
    boolean updateCategory(CategoryDTO categoryDTO);
    boolean deleteCategory(Long id);
    Page<CategoryDTO> getCategories (Pageable pageable);
    CategoryDTO getCategoryById(Long id);
    List<CategoryDTO> getNameAndIdCategories();

}
