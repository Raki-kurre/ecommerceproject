package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.Kafka.CategoryEvent;
import com.project.Kafka.KafkaProducerService;
import com.project.entity.Category;
import com.project.repository.CategoryRepository;
import com.project.repository.ProductRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository crepo;

    @Autowired
    private ProductRepository prepo;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    // ✅ SAVE CATEGORY
    public void saveCategory(Category c) {
        crepo.save(c);
    }
    public void deleteById(int id) {

        // 🔥 STEP 1: DELETE PRODUCTS UNDER CATEGORY
        prepo.deleteAllByCategory_Id(id);

        // 🔥 STEP 2: DELETE CATEGORY
        crepo.deleteById(id);
    }

    // ✅ SAVE CATEGORY + KAFKA
    public void saveCategory1(Category c) {
        crepo.save(c);

        CategoryEvent event = new CategoryEvent(
                c.getId(),
                c.getName(),
                "CATEGORY_CREATED"
        );

        kafkaProducerService.sendCategoryEvent(event);
    }

    // ✅ GET ALL
    public List<Category> getAll() {
        return crepo.findAll();
    }

    // ✅ FETCH BY ID
    public Optional<Category> fetchbyId(int id) {
        return crepo.findById(id);
    }

    // 🔥 SAFE DELETE (FIXED)
    public void deletebyId(int id) {

        boolean hasProducts = prepo.existsByCategory_Id(id);

        if (hasProducts) {
            throw new RuntimeException(
                "Cannot delete category. Products exist under this category."
            );
        }

        crepo.deleteById(id);
    }
}