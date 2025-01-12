package com.example.demo.repository;

import com.example.demo.entity.Atmosphere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtmosphereRepository extends JpaRepository<Atmosphere, Integer> {

    Optional<Atmosphere> findByHeightKm(int heightKm);

}
