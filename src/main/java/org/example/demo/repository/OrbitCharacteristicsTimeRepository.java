package org.example.demo.repository;

import org.example.demo.entity.OrbitCharacteristicsTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrbitCharacteristicsTimeRepository extends JpaRepository<OrbitCharacteristicsTime, Integer> {

    List<OrbitCharacteristicsTime> findAllByInitialOrbitAndFinalOrbitAndSpeedAndAlfaAndGamma(
            double initialOrbit, double finalOrbit, double speed, double alfa, double gamma
    );
}
