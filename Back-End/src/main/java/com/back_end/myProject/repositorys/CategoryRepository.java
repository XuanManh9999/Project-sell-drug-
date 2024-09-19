package com.back_end.myProject.repositorys;

import com.back_end.myProject.dto.CategoryDTO;
import com.back_end.myProject.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    @Query("SELECT new com.back_end.myProject.dto.CategoryDTO(c.id, c.name) FROM Category c")
    List<CategoryDTO> findAllCategoryIdAndName();
}
