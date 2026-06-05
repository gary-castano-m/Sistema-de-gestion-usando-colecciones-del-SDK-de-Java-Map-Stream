# 🐾 Sistema de Gestión de Veterinaria

**Asignatura:** Estructuras de Datos  
**Unidad:** III — Colecciones SDK Java, Map y Stream  
**Caso:** 8 — Veterinaria  
**Entidad principal:** `MascotaAtencion` · **Identificador:** `codigoAtencion`

---

## ¿Qué hace este sistema?

Gestiona las atenciones médicas de una clínica veterinaria. Cuando una mascota llega a consulta, el sistema la registra, la coloca en una **cola de espera**, la atiende en orden de llegada y guarda un **historial** de las atenciones procesadas. Todo el flujo está respaldado por cuatro colecciones del SDK de Java trabajando en paralelo sobre los mismos objetos.

---

## Estructura del proyecto

```
veterinaria/
├── MascotaAtencion.java     ← Entidad principal con 6 atributos, equals y hashCode por codigoAtencion
├── GestorVeterinaria.java   ← Lógica del sistema: List, Queue, Deque, Map y operaciones Stream
└── Main.java                ← Menú de consola interactivo con 15 opciones
```

## Requisitos

- Java 17 o superior
- No requiere dependencias externas ni frameworks

---

## Menú de opciones

| # | Opción | Colección / API |
|---|--------|----------------|
| 1 | Registrar atención | List + Queue + Map |
| 2 | Ver todas las atenciones | List |
| 3 | Ver atenciones pendientes | Queue |
| 4 | Procesar siguiente atención | Queue → Deque |
| 5 | Ver historial de procesadas | Deque |
| 6 | Buscar por código de atención | Map O(1) |
| 7 | Buscar por nombre o propietario | Stream `filter` + `findFirst` |
| 8 | Filtrar por estado o especie | Stream `filter` |
| 9 | Ordenar atenciones | Stream `sorted` |
| 10 | Ver estadísticas | Stream `groupingBy` + `counting` |
| 11 | Ver agrupamientos por especie | Stream `groupingBy` |
| 12 | Cancelar atención pendiente | Queue `removeIf` |
| 13 | Deshacer último procesamiento | Deque → Queue |
| 14 | Ver cantidad de elementos | `size()` de cada colección |
| 15 | Salir | — |

---

## Colecciones utilizadas

| Colección | Implementación | Principio | Rol en el sistema |
|-----------|---------------|-----------|-------------------|
| `List` | `ArrayList` | Acceso indexado | Registro general e inmutable de todas las atenciones |
| `Queue` | `LinkedList` | FIFO | Cola de atenciones pendientes por atender |
| `Deque` | `ArrayDeque` | LIFO (pila) | Historial de atenciones procesadas |
| `Map` | `HashMap` | Clave-Valor | Búsqueda O(1) por `codigoAtencion` |
| `Stream` | SDK Java | Funcional | Filtros, ordenamientos, agrupamientos y estadísticas |

> Las cuatro colecciones almacenan **referencias al mismo objeto** `MascotaAtencion`.  
> Cambiar el estado de una atención se refleja automáticamente en todas las colecciones.

---

## Flujo de una atención

```
Registro         →   List + Queue + Map   (estado: PENDIENTE)
     ↓
Procesar (FIFO)  →   sale de Queue        (estado: PROCESADO)
     ↓                                          ↓
Deshacer (LIFO)  ←   sale de Deque        entra a Deque
     ↓
Cancelar         →   sale de Queue        (estado: CANCELADO)
                     permanece en List y Map como evidencia
```


## Atributos de MascotaAtencion

| Atributo | Tipo | Descripción |
|----------|------|-------------|
| `codigoAtencion` | `String` | Identificador único — clave del `Map` |
| `nombreMascota` | `String` | Nombre de la mascota |
| `especie` | `String` | Perro, Gato, Ave, Reptil, Otro |
| `nombrePropietario` | `String` | Nombre del dueño |
| `motivoConsulta` | `String` | Razón de la visita |
| `estado` | `String` | `PENDIENTE` / `PROCESADO` / `CANCELADO` |

---

## Operaciones Stream implementadas

```java
// Filtrar por estado
atenciones.stream()
    .filter(a -> a.getEstado().equalsIgnoreCase(estado))
    .collect(Collectors.toList());

// Estadisticas agrupadas
atenciones.stream()
    .collect(Collectors.groupingBy(
        MascotaAtencion::getEstado,
        Collectors.counting()
    ));

// Validaciones
atenciones.stream().anyMatch(a -> a.getEstado().equals("PENDIENTE"));
atenciones.stream().allMatch(a -> a.getCodigoAtencion() != null);
atenciones.stream().noneMatch(a -> a.getEstado().equals("CANCELADO"));
```

## Reglas de negocio

- No se pueden registrar dos atenciones con el mismo `codigoAtencion`
- Solo se pueden cancelar atenciones en estado `PENDIENTE`
- Al cancelar, la atención sale de la `Queue` pero permanece en `List` y `Map`
- El deshacer solo aplica sobre el último elemento procesado (cima del `Deque`)

---

## Autor

Gary José Castaño Molina  
Ingeniería de Software  
Cod. 7502420050