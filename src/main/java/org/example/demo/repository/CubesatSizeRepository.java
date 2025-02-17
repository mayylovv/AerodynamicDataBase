package org.example.demo.repository;

import org.example.demo.entity.CubesatSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CubesatSizeRepository extends JpaRepository<CubesatSize, Integer> {

    Optional<CubesatSize> findByName(String name);

}
