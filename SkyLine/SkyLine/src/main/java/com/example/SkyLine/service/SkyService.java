package com.example.SkyLine.service;

import com.example.SkyLine.model.Sky;
import com.example.SkyLine.repository.SkyRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkyService {
    @Autowired
    private SkyRepo skyRepository;

    public List<Sky> getAllSkies() {
        return skyRepository.findAll();
    }

    public Optional<Sky> getSkyById(int id) {
        return skyRepository.findById(id);
    }

    public Optional<Sky> getSkyByEmail(String email) {
        return skyRepository.findByEmail(email);
    }

    public Sky createOrUpdateSky(Sky sky) {
        return skyRepository.save(sky);
    }

    public void deleteSky(int id) {
        skyRepository.deleteById(id);
    }

    public void deleteSkyByEmail(String email) {
        Optional<Sky> sky = skyRepository.findByEmail(email);
        sky.ifPresent(value -> skyRepository.delete(value));
    }

    public Sky updateSkyByEmail(String email, Sky sky) {
        Optional<Sky> existingSky = skyRepository.findByEmail(email);
        if (existingSky.isPresent()) {
            sky.setId(existingSky.get().getId());
            return skyRepository.save(sky);
        } else {
            return null;
        }
    }
}
