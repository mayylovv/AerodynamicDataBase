package com.example.demo.repository;

import com.example.demo.entity.FormNtu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormNtuRepository extends JpaRepository<FormNtu, Integer> {
}
