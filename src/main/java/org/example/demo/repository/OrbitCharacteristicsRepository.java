package org.example.demo.repository;

import org.example.demo.entity.OrbitCharacteristics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrbitCharacteristicsRepository extends JpaRepository<OrbitCharacteristics, Integer> {

    List<OrbitCharacteristics> findAllByOrbitAndSpeedAndAlfa(double orbit, double speed, double alfa);
}
