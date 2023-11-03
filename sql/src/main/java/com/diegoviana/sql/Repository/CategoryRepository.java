package com.diegoviana.sql.Repository;
//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.diegoviana.sql.Model.Category;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryByName(String categoryName);
    
    @Query(value = "SELECT * FRIM category c WHERE c.name = :categoryName", nativeQuery = true)
    Category findCategoryByNameSQL(@Param("categoryName") String categoryName);
}
