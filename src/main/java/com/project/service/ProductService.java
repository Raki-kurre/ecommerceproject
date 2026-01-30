package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.Kafka.KafkaProducerService;
import com.project.Kafka.ProductEvent;
import com.project.entity.Product;
import com.project.repository.CartItemRepository;
import com.project.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository prepo;

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    // ✅ SAVE PRODUCT
    public void saveProduct(Product p) {
        prepo.save(p);
    }

    // ✅ SAVE PRODUCT + KAFKA EVENT
    public void saveProduct1(Product p) {
        prepo.save(p);

        ProductEvent event = new ProductEvent(
                p.getId(),
                p.getName(),
                p.getPrice(),
                "PRODUCT_CREATED"
        );

        kafkaProducerService.sendProductEvent(event);
    }

    // ✅ GET ALL PRODUCTS
    public List<Product> getAll() {
        return prepo.findAll();
    }

    // 🔥 FIXED DELETE (NO FK ERROR)
    @Transactional
    public void deletebyId(long id) {

        // ✅ STEP 1: DELETE CART ITEMS FIRST
        cartItemRepo.deleteAllByProduct_Id(id);

        // ✅ STEP 2: DELETE PRODUCT
        prepo.deleteById(id);
    }

    // ✅ FETCH PRODUCT
    public Optional<Product> fetchbyId(long id) {
        return prepo.findById(id);
    }

    // ✅ VIEW PRODUCT
    public Product getProductById(Long id) {
        return prepo.findByIdWithCategory(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // ✅ GET PRODUCTS BY CATEGORY
    public List<Product> getProByCatId(int id) {
        return prepo.findAllByCategory_Id(id);
    }
}