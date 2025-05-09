package org.example.demo.repository;

import org.example.demo.entity.AerodynamicCharacteristics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AerodynamicCharacteristicsRepository extends JpaRepository<AerodynamicCharacteristics, Integer> {

    List<AerodynamicCharacteristics> findAllByCubesatSizeId(int cubesatSizeId);

    List<AerodynamicCharacteristics> findAllByNtuIdIn(Collection<Integer> ntuId);

    List<AerodynamicCharacteristics> findAllByNtuId(int ntuId);

    @Query(value = GET_ALL_FOR_TABLE, nativeQuery = true)
    List<Object[]> getAllForTable();

    String GET_ALL_FOR_TABLE =
    """
        SELECT
            aero.id,
            cubesat.name,
            ntu.name,
            orbit.orbit,
            orbit.speed,
            orbit.alfa,
            aero.force_x,
            aero.coefficient_x,
            aero.moment_x,
            aero.force_y,
            aero.coefficient_y,
            aero.moment_y,
            aero.velocity_head
        FROM aero_database.aerodynamic_characteristics aero
            JOIN aero_database.cubesat_size cubesat ON aero.cubesat_size_id = cubesat.id
            JOIN aero_database.orbit_characteristics orbit ON aero.orbit_id = orbit.id
            JOIN aero_database.characteristics_ntu ntu ON aero.ntu_id = ntu.id
    """;
}
