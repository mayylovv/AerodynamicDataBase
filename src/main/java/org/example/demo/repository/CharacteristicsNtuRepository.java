package org.example.demo.repository;

import org.example.demo.entity.CharacteristicsNtu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicsNtuRepository extends JpaRepository<CharacteristicsNtu, Integer> {
}