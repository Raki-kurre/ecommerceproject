package com.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.Address;
import com.project.entity.User;

public interface AddressRepository extends JpaRepository<Address, Long> {

    // 🔹 Latest address (used in cart / fallback)
    List<Address> findByUserOrderByIdDesc(User user);

    // 🔹 All addresses of user
    List<Address> findByUser(User user);

    // 🔥 Selected address (VERY IMPORTANT for orders & admin delivery)
    Address findByUserAndSelectedTrue(User user);
}