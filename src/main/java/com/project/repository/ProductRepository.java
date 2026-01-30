package com.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
        SELECT p FROM Product p
        JOIN FETCH p.category
        WHERE p.id = :id
    """)
    Optional<Product> findByIdWithCategory(@Param("id") Long id);

    List<Product> findAllByCategory_Id(int id);
    boolean existsByCategory_Id(int id);
   

    void deleteAllByCategory_Id(int id);
}