package org.example.demo.repository;

import org.example.demo.entity.CubesatSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CubesatSizeRepository extends JpaRepository<CubesatSize, Integer> {

    Optional<CubesatSize> findByName(String name);

    @Query(DELETE_BY_NAME)
    void removeByName(String name);

    String DELETE_BY_NAME= """
            SELECT
                ntu.id,
                ntu.name,
                form.name AS form,
                ntu.radius,
                ntu.length,
                ntu.thickness,
                material.name AS material,
                material.density
            FROM CharacteristicsNtu ntu
            JOIN ntu.form form
            JOIN ntu.material material
            """;

}
