package com.diegoviana.sql.Controller;

import java.util.ArrayList;
import java.util.List;

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
import com.diegoviana.sql.Model.Product;
import com.diegoviana.sql.Repository.CategoryRepository; 
import com.diegoviana.sql.Repository.ProductRepository; 



@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @GetMapping("/byName/{productName}")
    public Product getProductByName(@PathVariable String productName) {
        return productRepository.findProductByName(productName);
}

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            return productRepository.findByCategory(category);
        }
        return new ArrayList<>();
    }

    @GetMapping("/below-price/{maxPrice}")
    public List<Product> getProductsBelowMaxPrice(@PathVariable double maxPrice) {
        return productRepository.findProductsBelowMaxPrice(maxPrice);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {

    Category category = categoryRepository.findById(product.getCategory().getId()).orElse(null);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); 
        }

    product.setCategory(category);
    Product createdProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdProduct); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
    Product existingProduct = productRepository.findById(id).orElse(null);

    if (existingProduct != null) {
            existingProduct.setPrice(updatedProduct.getPrice());
            Product savedProduct = productRepository.save(existingProduct);
            return ResponseEntity.ok(savedProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); 
        }
}

    @DeleteMapping("/byCategory/{categoryId}")
    public ResponseEntity<String> deleteProductsByCategory(@PathVariable Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            List<Product> products = productRepository.findByCategory(category);
            productRepository.deleteAll(products);
            return ResponseEntity.ok("Produtos associados à categoria excluídos com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Categoria não encontrada com o ID: " + categoryId);
    }
}
