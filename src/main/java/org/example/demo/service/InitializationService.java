package org.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.example.demo.entity.Atmosphere;
import org.example.demo.entity.CharacteristicsNtu;
import org.example.demo.entity.CubesatSize;
import org.example.demo.entity.FormNtu;
import org.example.demo.entity.MaterialInfoEntity;
import org.example.demo.repository.AtmosphereRepository;
import org.example.demo.repository.CharacteristicsNtuRepository;
import org.example.demo.repository.CubesatSizeRepository;
import org.example.demo.repository.FormNtuRepository;
import org.example.demo.repository.MaterialInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InitializationService {
    private final CubesatSizeRepository cubesatSizeRepository;
    private final AtmosphereRepository atmosphereRepository;
    private final MaterialInfoRepository materialInfoRepository;
    private final FormNtuRepository formNtuRepository;
    private final CharacteristicsNtuRepository characteristicsNtuRepository;

    public CubesatSize saveCubesatSize(CubesatSize cubesatSize) {
        return cubesatSizeRepository.save(cubesatSize);
    }

    public CharacteristicsNtu saveCharacteristicsNtu(CharacteristicsNtu characteristicsNtu) {
        return characteristicsNtuRepository.save(characteristicsNtu);
    }

    public void saveMaterialInfo(MaterialInfoEntity materialInfoEntity) {
        materialInfoRepository.save(materialInfoEntity);
    }

    public List<String> getAllHeight() {
        return atmosphereRepository.findAll().stream().map(Atmosphere::getHeightKm).map(String::valueOf).toList();
    }

    public List<String> getAllMaterial() {
        return materialInfoRepository.findAll().stream().map(MaterialInfoEntity::getName).toList();
    }

    public List<String> getAllShapes() {
        return formNtuRepository.findAll().stream().map(FormNtu::getName).toList();
    }

    public int getIdFormByName(String name) {
        return formNtuRepository.findByName(name).get().getId();
    }

    public int getIdMaterialByName(String name) {
        return materialInfoRepository.findByName(name).get().getId();
    }

    public CubesatSize getCubesatById(int id) {
        return cubesatSizeRepository.findById(id).get();
    }

    public CharacteristicsNtu getCharacteristicsById(int id) {
        return characteristicsNtuRepository.findById(id).get();
    }

    public FormNtu getFormById(int id) {
        return formNtuRepository.findById(id).get();
    }
}
