package com.diegoviana.sql.Controller;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.diegoviana.sql.Model.Category;
import com.diegoviana.sql.Repository.CategoryRepository;

@RequestMapping("/categories")
@RestController
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @GetMapping("/byName/{categoryName}")
    public Category getCategoryByName(@PathVariable String categoryName) {
        return categoryRepository.findCategoryByName(categoryName);
    }

    @GetMapping("/byNameSQL/{categoryName}") 
    public Category getCategoryByNameSQL(@PathVariable String categoryName) {
        return categoryRepository.findCategoryByNameSQL(categoryName);
    }

    @PutMapping("/{id}")   
        public Category updateCategory(@PathVariable Long id, @RequestBody Category updateCategory) {
            Category existingCategory = categoryRepository.findById(id).orElse(null);
            if (existingCategory != null) {
                existingCategory.setName(updateCategory.getName());
                return categoryRepository.save(existingCategory);
            }
            return null;
        }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {

        Category existingCategory = categoryRepository.findCategoryByName(category.getName());
        if (existingCategory != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        
        Category createdCategory = categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdCategory);
    }
        

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            categoryRepository.deleteById(id);
            return ResponseEntity.ok("Categoria excluída com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Não é possível excluir a categoria devido a produtos associados.");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Categoria não encontrada com o ID: " + id);
        }
    }
}
