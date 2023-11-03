package com.diegoviana.sql.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.diegoviana.sql.Model.Product;
import com.diegoviana.sql.Model.Category;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(Category category);

    @Query(value = "SELECT * FROM product p WHERE p.price < :maxPrice", nativeQuery = true)
    List<Product> findProductsBelowMaxPrice(@Param("maxPrice") double maxPrice);

    @Query("SELECT p FROM Product p WHERE p.name = :productName")
    Product findProductByName(@Param("productName") String productName);
    
}
