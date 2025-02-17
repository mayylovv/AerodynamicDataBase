package org.example.demo.repository;

import org.example.demo.entity.AerodynamicCharacteristics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AerodynamicCharacteristicsRepository extends JpaRepository<AerodynamicCharacteristics, Integer> {
}
