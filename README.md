# Magneto's Recruitment Test

## Introducción

Magneto está en búsqueda de la mayor cantidad de mutantes para fortalecer su ejército en la lucha contra los X-Men. Como parte de su plan, te ha contratado para desarrollar un programa que detecte si un humano es un mutante analizando su secuencia de ADN.

El objetivo es crear una función que analice secuencias de ADN y determine si el individuo es mutante.

La función debe tener la siguiente firma:

```java
isMutant(String[] dna)
```

## Funcionamiento

El programa recibirá un arreglo de cadenas (Strings), donde cada elemento del arreglo representa una fila de una tabla de 6x6 que contiene la secuencia de ADN. Las letras en las cadenas pueden ser únicamente: **A**, **T**, **C**, **G**, que corresponden a las bases nitrogenadas del ADN.

Un humano será identificado como mutante si se encuentran **más de una secuencia** de cuatro letras iguales, ya sea de forma oblicua, horizontal o vertical.

Las filas de la matriz se ingresarán por teclado.

### Ejemplo de entrada:

- Una entrada típica sería algo como: `'ATCGTA'` (esto representa una fila de la matriz).

### Proceso:

1. El usuario ingresa las filas de la matriz de ADN una a una.
2. El programa analiza si hay más de una secuencia de cuatro letras iguales.
3. El resultado indica si el ADN pertenece a un mutante o no.

La detección debe realizarse en las tres direcciones: horizontal, vertical y oblicua.

## Ejecución

El proyecto ha sido deployado a Render y puede ser accedido mediante el siguiente link:

https://parcialprogramacionmutantes.onrender.com

### Endpoints

#### /mutant

- **POST** /mutant - Recibe un JSON con la matriz de ADN a verificar. Ejemplo:

```json
{
    "dna": [
        "ATGCGA",
        "CAGTGC",
        "TTATGT",
        "AGAAGG",
        "CCCCTA",
        "TCACTG"
    ]
}

```
- **GET** /mutant/all - Devuelve un JSON con todos los ADN verificados. Ejemplo:

```json
[
    {
        "id": 1,
        "dna": [
            "aaaaaa",
            "aaaaaa",
            "aaaaaa",
            "aaaaaa",
            "aaaaaa",
            "aaaaaa"
        ],
        "isMutant": true
    },
    {
        "id": 2,
        "dna": [
            "ATGCGA",
            "CAGTGC",
            "TTATGT",
            "AGAAGG",
            "CCCCTA",
            "TCACTG"
        ],
        "isMutant": true
    }
]

```
- **GET** /mutant/all/{id} - Devuelve un JSON con todos los datos del id especificado. Ejemplo:

```json
{
    "dna": [
        "aaaaaa",
        "aaaaaa",
        "aaaaaa",
        "aaaaaa",
        "aaaaaa",
        "aaaaaa"
    ],
    "isMutant": true,
    "id": 1
}
```
- **GET** /mutant/short/{id} - Devuelve un JSON con **SOLO** el ADN del id especificado. Ejemplo:

```json
{
    "dna": [
        "aaaaaa",
        "aaaaaa",
        "aaaaaa",
        "aaaaaa",
        "aaaaaa",
        "aaaaaa"
    ]
}
```

#### /stats

- **GET** /stats/all - Devuelve un JSON todas las stats de todos los ADN verificados. Ejemplo:

```json
[
    {
        "id": 1,
        "countMutantDNA": 54,
        "countHumanDNA": 0,
        "ratio": 1.0
    },
    {
        "id": 2,
        "countMutantDNA": 3,
        "countHumanDNA": 51,
        "ratio": 0.05555555555555555
    }
]
```
- **GET** /stats/all/{id} - Devuelve un JSON todas las stats del ADN especificado. Ejemplo:

```json
{
    "countMutantDNA": 3,
    "countHumanDNA": 51,
    "ratio": 0.05555555555555555
}
```

## Ejemplos de ADN

Ejemplo de matriz **MUTANTE**:

```json
{
    "dna": [
      "ATGCGA",
      "CAGTGC",
      "TTATGT",
      "AGAAAG",
      "CCCCTA",
      "TCACTG"
    ]
}
```

Ejemplo de matriz **NO MUTANTE**:

```json
{
    "dna": [
      "ATGGTG",
      "GTCTTA",
      "AATTGG",
      "ACTAGT",
      "GGATTC", 
      "AGGCAA"
    ]
}
```

## Pruebas Unitarias

Se incluyen casos de pruebas contemplando los errores de ingreso como las verificaciones con ADN tanto cortos como largos.
