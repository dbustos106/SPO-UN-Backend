package com.app.spoun.repository;

import com.app.spoun.domain.Role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository  extends JpaRepository<Role, Integer>{

    Page<Role> findAll(Pageable page);
    Optional<Role> findById(Integer id);
    Optional<Role> findByName(String name);
    Role save(Role role);
    void deleteById(Integer id);
    boolean existsById(Integer id);

}

