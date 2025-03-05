package org.example.demo.repository;

import org.example.demo.entity.CharacteristicsNtu;
import org.example.demo.entity.MaterialInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacteristicsNtuRepository extends JpaRepository<CharacteristicsNtu, Integer> {

    Boolean existsByName(String name);

    Optional<CharacteristicsNtu> findByName(String name);

    List<CharacteristicsNtu> findAllByMaterial(MaterialInfoEntity material);
}