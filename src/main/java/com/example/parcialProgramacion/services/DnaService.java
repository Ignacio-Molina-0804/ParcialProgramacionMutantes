package com.example.parcialProgramacion.services;

import com.example.parcialProgramacion.domain.dto.DnaAllDto;
import com.example.parcialProgramacion.domain.dto.DnaShortDto;
import com.example.parcialProgramacion.domain.entities.DnaEntity;
import com.example.parcialProgramacion.domain.entities.MutantResult;
import com.example.parcialProgramacion.repositories.DnaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DnaService {

    private final DnaRepository dnaRepository;

    private final StatsService statsService;

    public DnaService(DnaRepository dnaRepository, StatsService statsService) {
        this.dnaRepository = dnaRepository;
        this.statsService = statsService;
    }

    // Metodo para buscar una entidad por ID
    public DnaEntity findById(Long id) {
        return dnaRepository.findById(id).orElse(null);
    }

    // Metodo para guardar una entidad en la base de datos
    @Transactional
    public DnaEntity save(DnaEntity dnaEntity) {
        return dnaRepository.save(dnaEntity);
    }

    public List<DnaEntity> getAllDna() {
        return dnaRepository.findAll(); // Retorna todos los registros de la tabla DnaEntity
    }

    // Metodo para convertir de DnaAllDto a DnaEntity
    public DnaEntity convertToEntity(DnaAllDto dnaAllDto) {
        DnaEntity dnaEntity = new DnaEntity();
        // Mapea los campos de DnaAllDto a DnaEntity
        dnaEntity.setDna(dnaAllDto.getDna());
        dnaEntity.setIsMutant(dnaAllDto.getIsMutant());
        return dnaEntity;
    }

    // Metodo para convertir de DnaEntity a DnaShortDto
    public DnaAllDto convertToAllDto(DnaEntity dnaEntity) {
        DnaAllDto dnaAllDto = new DnaAllDto();
        // Mapea los campos de DnaEntity a DnaAllDto
        dnaAllDto.setDna(dnaEntity.getDna());
        dnaAllDto.setIsMutant(dnaEntity.getIsMutant());
        dnaAllDto.setID(dnaEntity.getId()); // Mapeo adicional si `ID` es necesario
        return dnaAllDto;
    }

    // Metodo para convertir de DnaEntity a DnaShortDto
    public DnaShortDto convertToShortDto(DnaEntity dnaEntity) {
        DnaShortDto dnaShortDto = new DnaShortDto();
        // Aquí mapeas los campos necesarios de DnaEntity a DnaShortDto
        dnaShortDto.setDna(dnaEntity.getDna());
        // Agrega los campos restantes
        return dnaShortDto;
    }

    // Metodo que verifica si el ADN es mutante
    public MutantResult isMutant(String[] dna) {
        int N = dna.length;  // Tamaño de la matriz NxN
        int countDnaMutant = 0;
        int countDnaHuman = 0;
        boolean isADnaMutant;

        // Verificaciones horizontales
        for (int row = 0; row < N; row++) {
            for (int i = 0; i < N - 3; i++) {
                if (dna[row].charAt(i) == dna[row].charAt(i + 1) &&
                        dna[row].charAt(i + 1) == dna[row].charAt(i + 2) &&
                        dna[row].charAt(i + 2) == dna[row].charAt(i + 3)) {
                    countDnaMutant++;
                } else {
                    countDnaHuman++;
                }
            }
        }

        // Verificaciones verticales
        for (int col = 0; col < N; col++) {
            for (int i = 0; i < N - 3; i++) {
                if (dna[i].charAt(col) == dna[i + 1].charAt(col) &&
                        dna[i + 1].charAt(col) == dna[i + 2].charAt(col) &&
                        dna[i + 2].charAt(col) == dna[i + 3].charAt(col)) {
                    countDnaMutant++;
                } else {
                    countDnaHuman++;
                }
            }
        }

        // Verificaciones diagonales (principales de izquierda a derecha)
        for (int i = 0; i < N - 3; i++) {
            for (int j = 0; j < N - 3; j++) {
                if (dna[i].charAt(j) == dna[i + 1].charAt(j + 1) &&
                        dna[i + 1].charAt(j + 1) == dna[i + 2].charAt(j + 2) &&
                        dna[i + 2].charAt(j + 2) == dna[i + 3].charAt(j + 3)) {
                    countDnaMutant++;
                } else {
                    countDnaHuman++;
                }
            }
        }

        // Verificaciones diagonales (desde abajo hacia arriba, derecha a izquierda)
        for (int i = 0; i < N - 3; i++) {
            for (int j = 3; j < N; j++) {
                if (dna[i].charAt(j) == dna[i + 1].charAt(j - 1) &&
                        dna[i + 1].charAt(j - 1) == dna[i + 2].charAt(j - 2) &&
                        dna[i + 2].charAt(j - 2) == dna[i + 3].charAt(j - 3)) {
                    countDnaMutant++;
                } else {
                    countDnaHuman++;
                }
            }
        }

        if (countDnaMutant > 1) {

            isADnaMutant = true;

        } else {

            isADnaMutant = false;

        }

        // Devolver ambos contadores
        return new MutantResult(isADnaMutant, countDnaMutant, countDnaHuman);
    }

    public DnaEntity processAndSave(DnaAllDto dnaAllDto) {
        // Obtener el ADN
        String[] dna = dnaAllDto.getDna();

        // Validar si el array es null
        if (dna == null) {
            throw new IllegalArgumentException("El array de ADN no puede ser null.");
        }

        // Validar si el array está vacío
        if (dna.length == 0) {
            throw new IllegalArgumentException("El array de ADN no puede estar vacío.");
        }

        // Validar si alguna de las filas es null
        for (String sequence : dna) {
            if (sequence == null) {
                throw new IllegalArgumentException("El array de ADN no puede contener secuencias null.");
            }
        }

        // Validar que sea una matriz NxN (todas las filas deben tener la misma longitud que el número de filas)
        int N = dna.length;
        for (String sequence : dna) {
            if (sequence.length() != N) {
                throw new IllegalArgumentException("El ADN debe ser una matriz NxN.");
            }
        }

        // Validar que solo se utilicen los caracteres permitidos (A, T, C, G)
        String validChars = "ATCG";
        for (String sequence : dna) {
            String upperSequence = sequence.toUpperCase();
            if (!upperSequence.chars().allMatch(c -> validChars.indexOf(c) != -1)) {
                throw new IllegalArgumentException("Las secuencias de ADN solo pueden contener los caracteres A, T, C y G.");
            }
        }

        // Lógica de detección de mutantes
        MutantResult result = isMutant(dna);

        // Crear entidad DNA y asignar si es mutante
        DnaEntity dnaEntity = convertToEntity(dnaAllDto);
        dnaEntity.setIsMutant(result.getIsADnaMutant());

        // Guardar en la base de datos el ADN
        DnaEntity savedEntity = dnaRepository.save(dnaEntity);

        // Actualizar estadísticas
        statsService.updateStats(result.getCountDnaMutant(), result.getCountDnaHuman());

        return savedEntity;
    }

}