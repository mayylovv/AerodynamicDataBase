package org.example.demo.repository;

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

    @Query(value = GET_ALL_FOR_TABLE, nativeQuery = true)
    List<Object[]> getAllForTable();

    String GET_ALL_FOR_TABLE =
            """
                    SELECT
                    ntu.id as id,
                    ntu.name as name,
                    form_ntu.name AS form,
                    ntu.radius as radius,
                    ntu.length as length,
                    ntu.thickness as thickness,
                    material_info.name AS material,
                    material_info.density
                    FROM aero_database.characteristics_ntu ntu
                    JOIN aero_database.form_ntu form_ntu ON ntu.form_id = form_ntu.id
                    JOIN aero_database.material_info material_info ON ntu.material_id = material_info.id
                    """;
}


