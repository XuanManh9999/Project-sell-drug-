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
            Page<Category> categoryDTOS = categoryService.getCategories(pageable);
            responseCustom = new ResponseCustom(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), categoryDTOS);
            return new ResponseEntity<>(responseCustom, HttpStatus.OK);
        }catch (Exception e) {
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
                     responseCustom = new ResponseCustom(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), isCreate);
                     return new ResponseEntity<>(responseCustom, HttpStatus.OK);
                 }else {
                     responseCustom = new ResponseCustom(HttpStatus.CONTINUE.value(), HttpStatus.CONTINUE.getReasonPhrase(), null);
                     return new ResponseEntity<>(responseCustom, HttpStatus.CONTINUE);
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
                    responseCustom = new ResponseCustom(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
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
}
