package org.example.demo.repository;

import org.example.demo.dto.NtuTableDto;
import org.example.demo.entity.FormNtu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormNtuRepository extends JpaRepository<FormNtu, Integer> {

    Optional<FormNtu> findByName(String name);

    boolean existsByName(String name);

    @Query(GET_ALL_FOR_TABLE)
    List<NtuTableDto> getAllForTable();

    String GET_ALL_FOR_TABLE= """
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


