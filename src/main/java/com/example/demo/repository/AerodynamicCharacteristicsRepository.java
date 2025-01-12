package com.example.demo.repository;

import com.example.demo.entity.AerodynamicCharacteristics;
import com.example.demo.entity.Atmosphere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AerodynamicCharacteristicsRepository extends JpaRepository<AerodynamicCharacteristics, Integer> {
}
