package org.example.demo.repository;

import org.example.demo.entity.MaterialInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialInfoRepository extends JpaRepository<MaterialInfoEntity, Integer>  {
}
