package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.entity.DeliveryConfig;

public interface DeliveryConfigRepository extends JpaRepository<DeliveryConfig, Long> {

}
