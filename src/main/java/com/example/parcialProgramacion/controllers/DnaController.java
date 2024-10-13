package com.example.parcialProgramacion.controllers;

import com.example.parcialProgramacion.domain.dto.DnaAllDto;
import com.example.parcialProgramacion.domain.dto.DnaShortDto;
import com.example.parcialProgramacion.domain.entities.DnaEntity;
import com.example.parcialProgramacion.services.DnaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/mutant")
public class DnaController {

    private final DnaService dnaService;

    public DnaController(DnaService dnaService) {
        this.dnaService = dnaService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<DnaEntity>> getAllDna() {
        List<DnaEntity> dnaList = dnaService.getAllDna();
        return ResponseEntity.ok(dnaList); // Retorna la lista de entidades en formato JSON
    }

    @GetMapping("/full/{id}")
    public ResponseEntity<DnaAllDto> getAllDataById(@PathVariable Long id) {
        DnaEntity dnaEntity = dnaService.findById(id);
        if (dnaEntity == null) {
            return ResponseEntity.notFound().build();
        }
        // Convertir la entidad a DnaAllDto
        DnaAllDto data = dnaService.convertToAllDto(dnaEntity);
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/short/{id}")
    public ResponseEntity<DnaShortDto> getShortDataById(@PathVariable Long id) {
        DnaEntity dnaEntity = dnaService.findById(id);
        if (dnaEntity == null) {
            return ResponseEntity.notFound().build();
        }
        // Convertir la entidad a DnaShortDto
        DnaShortDto data = dnaService.convertToShortDto(dnaEntity);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody DnaAllDto dnaAllDto) {
        try {
            // Procesar el ADN y guardar los resultados
            DnaEntity createdEntity = dnaService.processAndSave(dnaAllDto);

            if (createdEntity == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No se pudo procesar el ADN.");
            }

            // Convertir la entidad guardada a DnaAllDto
            DnaAllDto createdData = dnaService.convertToAllDto(createdEntity);

            // Devolver CREATED (201) con la URI del recurso recién creado
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(createdData);

        } catch (IllegalArgumentException e) {
            // Capturar excepciones de validación y devolver un mensaje de error con código 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            // Capturar cualquier otra excepción y devolver un error genérico
            e.printStackTrace();  // Registrar el error para más detalles
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ha ocurrido un error inesperado: " + e.getMessage());
        }
    }

}