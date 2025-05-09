package org.example.demo.repository;

import org.example.demo.entity.TimeCharacteristics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeCharacteristicsRepository extends JpaRepository<TimeCharacteristics, Integer> {

    @Query(value = GET_ALL_FOR_TABLE, nativeQuery = true)
    List<Object[]> getAllForTable();

    String GET_ALL_FOR_TABLE = """
            SELECT
                time.id,
                cubesat.name,
                ntu.name,
                orbit.initial_orbit,
                orbit.final_orbit,
                orbit.speed,
                orbit.alfa,
                orbit.gamma,
                time.land_time
            FROM aero_database.time_characteristics time
                JOIN aero_database.cubesat_size cubesat ON time.cubesat_size_id = cubesat.id
                JOIN aero_database.orbit_characteristics_time orbit on time.orbit_id_time = orbit.id
                JOIN aero_database.characteristics_ntu ntu ON time.ntu_id = ntu.id
            """;
}
