package veterinaria;

import java.util.*;
import java.util.stream.*;

/**
 * Gestor principal del sistema de Veterinaria.
 *
 * Colecciones utilizadas:
 *  - List<MascotaAtencion>   atenciones      → registro general de todas las atenciones
 *  - Queue<MascotaAtencion>  pendientes       → cola FIFO de atenciones por atender
 *  - Deque<MascotaAtencion>  historial        → pila LIFO de atenciones procesadas
 *  - Map<String, MascotaAtencion> indice      → búsqueda rápida por código de atención
 */
public class GestorVeterinaria {

    // Colecciones del SDK de Java
    private final List<MascotaAtencion>            atenciones  = new ArrayList<>();
    private final Queue<MascotaAtencion>           pendientes  = new LinkedList<>();
    private final Deque<MascotaAtencion>           historial   = new ArrayDeque<>();
    private final Map<String, MascotaAtencion>     indice      = new HashMap<>();


    // 1. REGISTRAR ATENCIÓN
    /**
     * Registra una nueva atención en el sistema.
     * La agrega a List, Queue y Map simultáneamente.
     * Valida que el código no esté duplicado usando Map.containsKey().
     */
    public void registrarAtencion(MascotaAtencion atencion) {
        if (indice.containsKey(atencion.getCodigoAtencion())) {
            throw new IllegalArgumentException(
                    "Ya existe una atención con el código: " + atencion.getCodigoAtencion());
        }
        atenciones.add(atencion);          // List: registro general
        pendientes.offer(atencion);         // Queue: cola de pendientes (FIFO)
        indice.put(atencion.getCodigoAtencion(), atencion); // Map: búsqueda rápida
    }


    // 2. VER TODOS LOS REGISTROS (List)

    public List<MascotaAtencion> obtenerTodasLasAtenciones() {

        return Collections.unmodifiableList(atenciones);

    }


    // 3. VER PENDIENTES (Queue)

    public Queue<MascotaAtencion> obtenerPendientes() {

        return pendientes;

    }

    public MascotaAtencion verSiguientePendiente() {

        return pendientes.peek();

    }


    // 4. PROCESAR SIGUIENTE ATENCIÓN (Queue → Deque)

    /**
     * Saca el primer elemento de la Queue (FIFO) y lo empuja al Deque (LIFO).
     * Cambia el estado de la atención a PROCESADO.
     */
    public MascotaAtencion procesarSiguiente() {
        MascotaAtencion procesado = pendientes.poll(); // FIFO: retira el primero
        if (procesado == null) {
            throw new IllegalStateException("No hay atenciones pendientes para procesar.");
        }
        procesado.setEstado("PROCESADO");
        historial.push(procesado); // LIFO: apila en el historial
        return procesado;
    }


    // 5. VER HISTORIAL (Deque)

    public Deque<MascotaAtencion> obtenerHistorial() {

        return historial;

    }

    public MascotaAtencion verUltimoProcesado() {

        return historial.peek(); // consulta sin retirar

    }


    // 6. BUSCAR POR CÓDIGO (Map)


    /**
     * Búsqueda O(1) usando Map.get() con el identificador principal.
     */
    public MascotaAtencion buscarPorCodigo(String codigoAtencion) {
        if (!indice.containsKey(codigoAtencion)) {
            throw new NoSuchElementException(
                    "No existe atención con código: " + codigoAtencion);
        }
        return indice.get(codigoAtencion);
    }


    // 7. BUSCAR POR NOMBRE DE MASCOTA (Stream)

    /**
     * Búsqueda por nombre usando Stream.filter() + findFirst().
     * Se usa Stream porque el nombre no es el identificador principal.
     */
    public Optional<MascotaAtencion> buscarPorNombreMascota(String nombre) {
        return atenciones.stream()
                .filter(a -> a.getNombreMascota().equalsIgnoreCase(nombre))
                .findFirst();
    }

    /**
     * Busca por nombre del propietario (puede devolver varias coincidencias).
     */
    public List<MascotaAtencion> buscarPorPropietario(String nombrePropietario) {
        return atenciones.stream()
                .filter(a -> a.getNombrePropietario().equalsIgnoreCase(nombrePropietario))
                .collect(Collectors.toList());
    }

    // 8. FILTRAR POR ESTADO / ESPECIE (Stream)

    public List<MascotaAtencion> filtrarPorEstado(String estado) {
        return atenciones.stream()
                .filter(a -> a.getEstado().equalsIgnoreCase(estado))
                .collect(Collectors.toList());
    }

    public List<MascotaAtencion> filtrarPorEspecie(String especie) {
        return atenciones.stream()
                .filter(a -> a.getEspecie().equalsIgnoreCase(especie))
                .collect(Collectors.toList());
    }


    // 9. ORDENAR (Stream)

    public List<MascotaAtencion> ordenarPorNombreMascota() {
        return atenciones.stream()
                .sorted(Comparator.comparing(MascotaAtencion::getNombreMascota))
                .collect(Collectors.toList());
    }

    public List<MascotaAtencion> ordenarPorCodigoDescendente() {
        return atenciones.stream()
                .sorted(Comparator.comparing(MascotaAtencion::getCodigoAtencion).reversed())
                .collect(Collectors.toList());
    }

    // 10. ESTADÍSTICAS (Stream + Collectors)

    public Map<String, Long> estadisticasPorEstado() {
        return atenciones.stream()
                .collect(Collectors.groupingBy(MascotaAtencion::getEstado, Collectors.counting()));
    }

    public Map<String, Long> estadisticasPorEspecie() {
        return atenciones.stream()
                .collect(Collectors.groupingBy(MascotaAtencion::getEspecie, Collectors.counting()));
    }

    public long contarPorEstado(String estado) {
        return atenciones.stream()
                .filter(a -> a.getEstado().equalsIgnoreCase(estado))
                .count();
    }

    public boolean hayPendientes() {
        return atenciones.stream()
                .anyMatch(a -> a.getEstado().equalsIgnoreCase("PENDIENTE"));
    }

    public boolean todasTienenCodigo() {
        return atenciones.stream()
                .allMatch(a -> a.getCodigoAtencion() != null && !a.getCodigoAtencion().isBlank());
    }

    public boolean noHayCancelados() {
        return atenciones.stream()
                .noneMatch(a -> a.getEstado().equalsIgnoreCase("CANCELADO"));
    }


    // 11. AGRUPAMIENTOS (Stream + Collectors.groupingBy)

    public Map<String, List<MascotaAtencion>> agruparPorEspecie() {
        return atenciones.stream()
                .collect(Collectors.groupingBy(MascotaAtencion::getEspecie));
    }

    public Map<String, List<MascotaAtencion>> agruparPorEstado() {
        return atenciones.stream()
                .collect(Collectors.groupingBy(MascotaAtencion::getEstado));
    }


    // 12. CANCELAR ATENCIÓN PENDIENTE

    /**
     * Cancela una atención pendiente.
     * - Actualiza estado a CANCELADO.
     * - La elimina de la Queue con removeIf().
     * - La mantiene en List y Map (evidencia de registro).
     */
    public void cancelarAtencion(String codigoAtencion) {
        MascotaAtencion atencion = indice.get(codigoAtencion);
        if (atencion == null) {
            throw new IllegalArgumentException(
                    "No existe atención con código: " + codigoAtencion);
        }
        if (!atencion.getEstado().equalsIgnoreCase("PENDIENTE")) {
            throw new IllegalStateException(
                    "Solo se pueden cancelar atenciones PENDIENTES. Estado actual: " + atencion.getEstado());
        }
        atencion.setEstado("CANCELADO");
        pendientes.removeIf(a -> a.getCodigoAtencion().equalsIgnoreCase(codigoAtencion));
    }


    // 13. DESHACER ÚLTIMO PROCESAMIENTO (Deque → Queue)

    /**
     * Saca el último elemento del historial (LIFO) y lo regresa a la cola de pendientes.
     * Cambia el estado de PROCESADO a PENDIENTE.
     */
    public MascotaAtencion deshacerUltimoProcesamiento() {
        if (historial.isEmpty()) {
            throw new IllegalStateException("El historial está vacío. No hay nada que deshacer.");
        }
        MascotaAtencion ultimo = historial.pop(); // LIFO: saca el último
        ultimo.setEstado("PENDIENTE");
        pendientes.offer(ultimo); // regresa a la cola de pendientes
        return ultimo;
    }

    // 14. CANTIDADES

    public int totalRegistros()  { return atenciones.size(); }
    public int totalPendientes() { return pendientes.size(); }
    public int totalHistorial()  { return historial.size(); }
    public int totalIndice()     { return indice.size(); }


    // Utilidad: obtener lista de nombres usando Stream.map()

    public List<String> obtenerNombresMascotas() {
        return atenciones.stream()
                .map(MascotaAtencion::getNombreMascota)
                .collect(Collectors.toList());
    }

    // Reconstruir Map a partir de List usando Collectors.toMap()
    public Map<String, MascotaAtencion> reconstruirIndice() {
        return atenciones.stream()
                .collect(Collectors.toMap(
                        MascotaAtencion::getCodigoAtencion,
                        a -> a,
                        (existente, repetido) -> existente
                ));
    }
}
