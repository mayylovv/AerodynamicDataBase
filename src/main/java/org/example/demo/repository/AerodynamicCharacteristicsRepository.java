package org.example.demo.repository;

import org.example.demo.entity.AerodynamicCharacteristics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AerodynamicCharacteristicsRepository extends JpaRepository<AerodynamicCharacteristics, Integer> {

    List<AerodynamicCharacteristics> findAllByCubesatSizeId(int cubesatSizeId);

    List<AerodynamicCharacteristics> findAllByNtuIdIn(Collection<Integer> ntuId);

    List<AerodynamicCharacteristics> findAllByNtuId(int ntuId);
}
