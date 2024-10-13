package com.example.parcialProgramacion.controllers;

import com.example.parcialProgramacion.domain.dto.StatsAllDto;
import com.example.parcialProgramacion.domain.entities.StatsEntity;
import com.example.parcialProgramacion.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/stats")
@RestController
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping("/all")
    public ResponseEntity<List<StatsEntity>> getAllStats() {

        List<StatsEntity> statsList = statsService.findAllStats();
        return ResponseEntity.ok(statsList);

    }

    @GetMapping("/all/{id}")
    public ResponseEntity<StatsAllDto> getAllDataById(@PathVariable Long id) {
        StatsEntity statsEntity = statsService.findById(id);
        if (statsEntity == null) {
            return ResponseEntity.notFound().build();
        }
        // Convertir la entidad a DnaAllDto
        StatsAllDto data = statsService.convertToDto(statsEntity);
        return ResponseEntity.ok().body(data);
    }

}
