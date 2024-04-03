package com.example.SkyLine.controller;

import com.example.SkyLine.model.Sky;
import com.example.SkyLine.service.SkyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/skies")
public class SkyController {

    @Autowired
    private SkyService skyService;

    @GetMapping
    public List<Sky> getAllSkies() {
        return skyService.getAllSkies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sky> getSkyById(@PathVariable int id) {
        Optional<Sky> sky = skyService.getSkyById(id);
        return sky.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<Sky> getSkyByEmail(@RequestParam String email) {
        Optional<Sky> sky = skyService.getSkyByEmail(email);
        return sky.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSky(@RequestBody Sky sky) {
        Optional<Sky> existingSkyOptional = skyService.getSkyByEmail(sky.getEmail());
        if (existingSkyOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists. Please use a different email.");
        } else {
            try {
                Sky createdSky = skyService.createOrUpdateSky(sky);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdSky);
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid request. Please provide valid data.");
            }
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Sky> updateSky(@PathVariable int id, @RequestBody Sky sky) {
        sky.setId(id);
        Sky updatedSky = skyService.createOrUpdateSky(sky);
        return new ResponseEntity<>(updatedSky, HttpStatus.OK);
    }

    @PutMapping("/updateByEmail/{email}")
    public ResponseEntity<Sky> updateSkyByEmail(@PathVariable String email, @RequestBody Sky sky) {
        Optional<Sky> existingSky = skyService.getSkyByEmail(email);
        if (existingSky.isPresent()) {
            Sky updatedSky = skyService.updateSkyByEmail(email, sky);
            return new ResponseEntity<>(updatedSky, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSky(@PathVariable int id) {
        skyService.deleteSky(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteByEmail/{email}")
    public ResponseEntity<Void> deleteSkyByEmail(@PathVariable String email) {
        Optional<Sky> existingSky = skyService.getSkyByEmail(email);
        if (existingSky.isPresent()) {
            skyService.deleteSkyByEmail(email);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
