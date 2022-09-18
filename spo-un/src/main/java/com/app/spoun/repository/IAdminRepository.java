package com.app.spoun.repository;

import com.app.spoun.dao.AdminDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAdminRepository extends JpaRepository<AdminDAO, Integer> {
    Page<AdminDAO> findAll(Pageable page);
    Optional<AdminDAO> findById(Integer id);
    AdminDAO save(AdminDAO adminDAO);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}