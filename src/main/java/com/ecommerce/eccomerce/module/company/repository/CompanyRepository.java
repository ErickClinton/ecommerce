package com.ecommerce.eccomerce.module.company.repository;

import com.ecommerce.eccomerce.module.company.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {
    Optional<CompanyEntity> findByEmail(String email);
}
