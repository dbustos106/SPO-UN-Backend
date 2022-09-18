package com.app.spoun.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.spoun.models.Admin;

import java.util.Optional;

@Repository
public interface IAdminRepository extends JpaRepository<Admin, Integer>{
    Page<Admin> findAll(Pageable page);
    Optional<Admin> findById(Integer id);
    Admin save(Admin admin);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}