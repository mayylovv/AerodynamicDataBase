package org.example.demo.repository;

import org.example.demo.entity.FormNtu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormNtuRepository extends JpaRepository<FormNtu, Integer> {

    Optional<FormNtu> findByName(String name);

    boolean existsByName(String name);
}
