package com.example.parcialProgramacion.services;

import com.example.parcialProgramacion.domain.dto.DnaAllDto;
import com.example.parcialProgramacion.repositories.DnaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class DnaServiceTest {

    @InjectMocks
    private DnaService dnaService;

    @Mock
    private DnaRepository dnaRepository;

    @Mock
    private StatsService statsService;  // Asegúrate de mockear StatsService

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa los mocks
    }

    // CASOS DE MANEJO DE ERRORES

    // 1. Caso: Recibir un array vacío
    @Test
    void whenDnaArrayIsEmpty_thenThrowIllegalArgumentException() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(new String[]{});

        assertThrows(IllegalArgumentException.class, () -> {
            dnaService.processAndSave(dnaAllDto);
        });
    }


    // 2. Caso: Recibir un array de NxM en vez de NxN
    @Test
    void whenDnaArrayIsNotNxN_thenThrowIllegalArgumentException() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(new String[]{"ATCG", "CAGT", "TTGA"});  // 3x4

        assertThrows(IllegalArgumentException.class, () -> {
            dnaService.processAndSave(dnaAllDto);
        });
    }

    // 3. Caso: Recibir un array de números (entre comillas para que sean string)
    @Test
    void whenDnaArrayContainsNumbers_thenThrowIllegalArgumentException() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(new String[]{"1234", "5678", "9123", "4567"});

        assertThrows(IllegalArgumentException.class, () -> {
            dnaService.processAndSave(dnaAllDto);
        });
    }

    // 4. Caso: Recibir null
    @Test
    void whenDnaArrayIsNull_thenThrowIllegalArgumentException() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(null);

        assertThrows(IllegalArgumentException.class, () -> {
            dnaService.processAndSave(dnaAllDto);
        });
    }

    // 5. Caso: Recibir un array de NxN de nulls
    @Test
    void whenDnaArrayContainsNulls_thenThrowIllegalArgumentException() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(new String[]{null, null, null, null});

        assertThrows(IllegalArgumentException.class, () -> {
            dnaService.processAndSave(dnaAllDto);
        });
    }

    // 6. Caso: Recibir un array de NxN con letras distintas a las permitidas {“A”,“T”,“C”,“G”}
    @Test
    void whenDnaArrayContainsInvalidLetters_thenThrowIllegalArgumentException() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(new String[]{"BXCG", "HYTC", "XWAV", "QCZG"});

        assertThrows(IllegalArgumentException.class, () -> {
            dnaService.processAndSave(dnaAllDto);
        });
    }

    // CASOS FACTIBLES PROPUESTOS POR EL PROFESOR

    //// Casos Mutantes

    @Test
    void IsMutant1() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(new String[]{
                "AAAA",  // Secuencia horizontal de "A"
                "CCCC",
                "TGAG",
                "GGTC"
        });

        boolean isMutant = dnaService.isMutant(dnaAllDto.getDna()).getIsADnaMutant();
        assertTrue(isMutant, "Se esperaba que el ADN fuera mutante.");
    }

    @Test
    void IsMutant2() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(new String[]{
                "TGAC",
                "AGCC",  // Secuencia diagonal inversa de "G"
                "TGAC",
                "GGTC"
        });

        boolean isMutant = dnaService.isMutant(dnaAllDto.getDna()).getIsADnaMutant();
        assertTrue(isMutant, "Se esperaba que el ADN fuera mutante.");
    }

    @Test
    void IsMutant3() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(new String[]{
                "AAAA",
                "AAAA",  // Secuencia diagonal inversa de "G"
                "AAAA",
                "AAAA"
        });

        boolean isMutant = dnaService.isMutant(dnaAllDto.getDna()).getIsADnaMutant();
        assertTrue(isMutant, "Se esperaba que el ADN fuera mutante.");
    }


    //// Casos no mutantes

    @Test
    void IsMutant4() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(new String[]{
                "AAAT",
                "AACC",
                "AAAC",
                "CGGG"
        });

        boolean isMutant = dnaService.isMutant(dnaAllDto.getDna()).getIsADnaMutant();
        assertFalse(isMutant, "Se esperaba que el ADN no fuera mutante.");
    }

    @Test
    void IsMutant5() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(new String[]{
                "TGAC",
                "ATCC",
                "TAGA",
                "GGTC"
        });

        boolean isMutant = dnaService.isMutant(dnaAllDto.getDna()).getIsADnaMutant();
        assertFalse(isMutant, "Se esperaba que el ADN no fuera mutante.");
    }

    //// Casos Largos

    @Test
    void IsMutant6() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(new String[]{
                "TCGGGTGAT",
                "TGATCCTTT",
                "TACGAGTGA",
                "AAATGTACG",
                "ACGAGTGCT",
                "AGACACATG",
                "GAATTCCAA",
                "ACTACGACC",
                "TGAGTATCC"
        });

        boolean isMutant = dnaService.isMutant(dnaAllDto.getDna()).getIsADnaMutant();
        assertTrue(isMutant, "Se esperaba que el ADN no fuera mutante.");
    }

    @Test
    void IsMutant7() {
        DnaAllDto dnaAllDto = new DnaAllDto();
        dnaAllDto.setDna(new String[]{
                "TTTTTTTTT",
                "TTTTTTTTT",
                "TTTTTTTTT",
                "TTTTTTTTT",
                "CCGACCAGT",
                "GGCACTCCA",
                "AGGACACTA",
                "CAAAGGCAT",
                "GCAGTCCCC"
        });

        boolean isMutant = dnaService.isMutant(dnaAllDto.getDna()).getIsADnaMutant();
        assertTrue(isMutant, "Se esperaba que el ADN no fuera mutante.");
    }


}