package com.back_end.myProject.controller;

import com.back_end.myProject.dto.CategoryDTO;
import com.back_end.myProject.entities.Category;
import com.back_end.myProject.service.ICategory;
import com.back_end.myProject.utils.ResponseCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    @Autowired
    private ICategory categoryService;


    @GetMapping(value = "/categories")
    public ResponseEntity<?> getAllCategories(Pageable pageable) {
        ResponseCustom responseCustom;
        try {
            Page<CategoryDTO> categoryDTOS = categoryService.getCategories(pageable);
            responseCustom = new ResponseCustom(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), categoryDTOS);
            return new ResponseEntity<>(responseCustom, HttpStatus.OK);
        }catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping(value = "/categories-load")
    public ResponseEntity<?> loadCategoriesFromDB() {
        ResponseCustom responseCustom;
        try {
            List<CategoryDTO> categoryDTOS = categoryService.getNameAndIdCategories();
            responseCustom = new ResponseCustom(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), categoryDTOS);
            return new ResponseEntity<>(responseCustom, HttpStatus.OK);
        }catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/category/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
        ResponseCustom responseCustom;
        try {
            CategoryDTO categoryDTO = categoryService.getCategoryById(id);
            if (categoryDTO != null) {
                responseCustom = new ResponseCustom(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), categoryDTO);
                return new ResponseEntity<>(responseCustom, HttpStatus.OK);
            }else {
                responseCustom = new ResponseCustom(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null);
                return new ResponseEntity<>(responseCustom, HttpStatus.NOT_FOUND);
            }
        }catch (Exception ex) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value = "/category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO categoryDTO) {
        ResponseCustom responseCustom;
        try {
            String name = categoryDTO.getName();
            String description = categoryDTO.getDescription();
            if (name == null || description == null) {
                responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
                return new ResponseEntity<>(responseCustom, HttpStatus.BAD_REQUEST);
            }else {
                 Boolean isCreate = categoryService.createCategory(categoryDTO);
                 if (isCreate) {
                     responseCustom = new ResponseCustom(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), isCreate);
                     return new ResponseEntity<>(responseCustom, HttpStatus.CREATED);
                 }else {
                     responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Add category failed. Category is exited", null);
                     return new ResponseEntity<>(responseCustom, HttpStatus.BAD_REQUEST);
                 }
            }

        }catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping(value = "/category")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        ResponseCustom responseCustom;
        try {
            Long id = categoryDTO.getId();
            String name = categoryDTO.getName();
            String description = categoryDTO.getDescription();
            if (id == null || name == null || description == null) {
                responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
                return new ResponseEntity<>(responseCustom, HttpStatus.BAD_REQUEST);
            }else {
                Boolean IsUpdate = categoryService.updateCategory(categoryDTO);
                if (IsUpdate) {
                    responseCustom = new ResponseCustom(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), IsUpdate);
                    return new ResponseEntity<>(responseCustom, HttpStatus.OK);
                }else {
                    responseCustom = new ResponseCustom(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), null);
                    return new ResponseEntity<>(responseCustom, HttpStatus.CONFLICT);
                }
            }

        }catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable ( name =  "id", required = true ) Long id) {
        ResponseCustom responseCustom;
        try {
            if (id == null || id <= 0) {
                responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
                return new ResponseEntity<>(responseCustom, HttpStatus.BAD_REQUEST);
            }else {
                Boolean isDelete = categoryService.deleteCategory(id);
                if (isDelete) {
                    responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Delete category done", null);
                    return new ResponseEntity<>(responseCustom, HttpStatus.OK);
                }else {
                    responseCustom = new ResponseCustom(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), null);
                    return new ResponseEntity<>(responseCustom, HttpStatus.CONFLICT);
                }
            }


        }catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Please remove products that are using the title, before deleting the title", null);
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
