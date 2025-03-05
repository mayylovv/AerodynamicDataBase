package org.example.demo.repository;

import org.example.demo.entity.MaterialInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialInfoRepository extends JpaRepository<MaterialInfoEntity, Integer> {

    Boolean existsByName(String name);

    Optional<MaterialInfoEntity> findByName(String name);
}
