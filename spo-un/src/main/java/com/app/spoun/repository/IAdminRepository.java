package com.app.spoun.repository;

import com.app.spoun.domain.Admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAdminRepository extends JpaRepository<Admin, Long>{

    Page<Admin> findAll(Pageable page);
    Optional<Admin> findById(Long id);
    Optional<Admin> findByUsername(String username);
    Admin save(Admin admin);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByUsername(String username);

    @Query(value = "SELECT * FROM admin WHERE admin.verification_code = ?1", nativeQuery = true)
    Optional<Admin> findByVerification_code(String code);

}