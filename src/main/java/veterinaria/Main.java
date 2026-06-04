package veterinaria;

import java.util.*;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final GestorVeterinaria gestor = new GestorVeterinaria();

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║        SISTEMA DE GESTIÓN - VETERINARIA                  ║");
        System.out.println("║   Colecciones SDK Java · Map · Stream                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        boolean ejecutando = true;
        while (ejecutando) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opción: ");
            System.out.println();
            switch (opcion) {
                case 1  -> registrarAtencion();
                case 2  -> verAtenciones();
                case 3  -> verPendientes();
                case 4  -> procesarSiguiente();
                case 5  -> verHistorial();
                case 6  -> buscarPorCodigo();
                case 7  -> buscarPorOtroCriterio();
                case 8  -> filtrarAtenciones();
                case 9  -> ordenarAtenciones();
                case 10 -> verEstadisticas();
                case 11 -> verAgrupamientos();
                case 12 -> cancelarAtencion();
                case 13 -> deshacerProcesamiento();
                case 14 -> verCantidades();
                case 15 -> { ejecutando = false; System.out.println("Hasta luego. ¡Cuide a sus mascotas!"); }
                default -> System.out.println(" Opción no válida. Intente nuevamente.");
            }
            if (ejecutando) pausar();
        }
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n──────────────────────────────────────────────────────────");
        System.out.println("  MENÚ PRINCIPAL");
        System.out.println("──────────────────────────────────────────────────────────");
        System.out.println("  1.  Registrar atención");
        System.out.println("  2.  Ver todas las atenciones registradas");
        System.out.println("  3.  Ver atenciones pendientes");
        System.out.println("  4.  Procesar siguiente atención");
        System.out.println("  5.  Ver historial de atenciones procesadas");
        System.out.println("  6.  Buscar por código de atención (Map)");
        System.out.println("  7.  Buscar por otro criterio (Stream)");
        System.out.println("  8.  Filtrar atenciones (Stream)");
        System.out.println("  9.  Ordenar atenciones (Stream)");
        System.out.println(" 10.  Ver estadísticas (Stream + Map)");
        System.out.println(" 11.  Ver agrupamientos (Stream + Map)");
        System.out.println(" 12.  Cancelar atención pendiente");
        System.out.println(" 13.  Deshacer último procesamiento");
        System.out.println(" 14.  Ver cantidad de elementos");
        System.out.println(" 15.  Salir");
        System.out.println("──────────────────────────────────────────────────────────");
    }

    private static void registrarAtencion() {
        System.out.println("=== REGISTRAR NUEVA ATENCIÓN ===");
        System.out.print("Código de atención: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        System.out.print("Nombre de la mascota: ");
        String nombreMascota = scanner.nextLine().trim();
        System.out.print("Especie: ");
        String especie = scanner.nextLine().trim();
        System.out.print("Nombre del propietario: ");
        String propietario = scanner.nextLine().trim();
        System.out.print("Motivo de consulta: ");
        String motivo = scanner.nextLine().trim();

        try {
            MascotaAtencion nueva = new MascotaAtencion(codigo, nombreMascota, especie, propietario, motivo);
            gestor.registrarAtencion(nueva);
            System.out.println(" registrado exitosamente:");
            System.out.println("   " + nueva);
        } catch (IllegalArgumentException e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    private static void verTodasLasAtenciones() {
        System.out.println("=== TODAS LAS ATENCIONES REGISTRADAS ===");
        var atenciones = gestor.obtenerTodasLasAtenciones();
        if (atenciones.isEmpty()) {
            System.out.println("No hay atenciones registradas.");
            return;
        }

        atenciones.forEach(a -> System.out.println("  " + a));
        System.out.println("  Total: " + atenciones.length());
    }

    private static void verPendientes() {
        System.out.println("=== ATENCIONES PENDIENTES (Cola FIFO) ===");
        var pendientes = gestor.obtenerPendientes();
        if (pendientes.isEmpty()) {
            System.out.println("No hay atenciones pendientes.");
            return;
        }

        System.out.println("  Siguiente a atender: " + pendientes.peek());
        System.out.println();
        pendientes.forEach(a -> System.out.println("  " + a));
        System.out.println("  Total pendientes: " + pendientes.size());
    }

    private static void procesarSiguiente() {
        System.out.println("=== PROCESAR SIGUIENTE ATENCIÓN ===");
        try {
            MascotaAtencion procesada = gestor.procesarSiguiente();
            System.out.println(" Atención procesada exitosamente:");
            System.out.println("   " + procesada);
            System.out.println("   Pendientes restantes: " + gestor.totalPendientes());
        } catch (IllegalStateException e) {
            System.out.println("Error  " + e.getMessage());
        }
    }

    private static void verHistorial() {
        System.out.println("=== HISTORIAL DE ATENCIONES PROCESADAS (Pila LIFO) ===");
        var historial = gestor.obtenerHistorial();
        if (historial.isEmpty()) {
            System.out.println("El historial está vacío.");
            return;
        }

        System.out.println("  Último procesado: " + historial.peek());
        System.out.println();
        historial.forEach(a -> System.out.println("  " + a));
        System.out.println("  Total en historial: " + historial.size());
    }

    private static void buscarPorCodigo() {
        System.out.println("=== BUSCAR POR CÓDIGO DE ATENCIÓN (Map) ===");
        System.out.print("Ingrese el código de atención: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        try {
            MascotaAtencion encontrada = gestor.buscarPorCodigo(codigo);
            System.out.println("✔  Atención encontrada:");
            System.out.println("   " + encontrada);
        } catch (NoSuchElementException e) {
            System.out.println("✘  " + e.getMessage());
        }
    }

    private static void buscarPorOtroCriterio() {
        System.out.println("=== BUSCAR POR OTRO CRITERIO (Stream) ===");
        System.out.println("  a) Por nombre de mascota");
        System.out.println("  b) Por nombre del propietario");
        System.out.print("Seleccione (a/b): ");
        String op = scanner.nextLine().trim().toLowerCase();

        if (op.equals("a")) {
            System.out.print("Nombre de la mascota: ");
            String nombre = scanner.nextLine().trim();
            gestor.buscarPorNombreMascota(nombre).ifPresentOrElse(
                    a -> { System.out.println(" Encontrada:"); System.out.println("   " + a); },
                    () -> System.out.println(" No se encontró ninguna mascota con ese nombre.")
            );
        } else if (op.equals("b")) {
            System.out.print("Nombre del propietario: ");
            String propietario = scanner.nextLine().trim();
            var lista = gestor.buscarPorPropietario(propietario);
            if (lista.isEmpty()) {
                System.out.println(" No se encontraron atenciones para ese propietario.");
            } else {
                System.out.println(" Atenciones encontradas:");
                lista.forEach(a -> System.out.println("   " + a));
            }
        } else {
            System.out.println("Opción no válida.");
        }
    }

    private static void filtrarAtenciones() {
        System.out.println("=== FILTRAR ATENCIONES (Stream) ===");
        System.out.println("  a) Por estado   (PENDIENTE / PROCESADO / CANCELADO)");
        System.out.println("  b) Por especie  (Perro / Gato / Ave / Reptil / Otro)");
        System.out.print("Seleccione (a/b): ");
        String op = scanner.nextLine().trim().toLowerCase();

        if (equals("a")) {
            System.out.print("Estado: ");
            String estado = scanner.nextLine().trim();
            var filtradas = gestor.filtrarPorEstado(estado);
            imprimirLista(filtradas, "No hay atenciones con ese estado.");
        } else if (op.equals("b")) {
            System.out.print("Especie: ");
            String especie = scanner.nextLine().trim();
            var filtradas = gestor.filtrarPorEspecie(especie);
            imprimirLista(filtradas, "No hay atenciones para esa especie.");
        } else {
            System.out.println("Opción no válida.");
        }
    }

    private static void ordenarAtenciones() {
        System.out.println("=== ORDENAR ATENCIONES (Stream) ===");
        System.out.println("  a) Por nombre de mascota (ascendente)");
        System.out.println("  b) Por código de atención (descendente)");
        System.out.print("Seleccione (a/b): ");
        String op = scanner.nextLine().trim().toLowerCase();

        if (op.equals("a")) {
            var ordenadas = gestor.ordenarPorNombreMascota();
            System.out.println("Ordenadas por nombre de mascota:");
            imprimirLista(ordenadas, "No hay atenciones registradas.");
        } else if (op.equals("b")) {
            var ordenadas = gestor.ordenarPorCodigoDescendente();
            System.out.println("Ordenadas por código (descendente):");
            imprimirLista(ordenadas, "No hay atenciones registradas.");
        } else {
            System.out.println("Opción no válida.");
        }
    }

    private static void verEstadisticas() {
        System.out.println("=== ESTADÍSTICAS DEL SISTEMA (Stream + Map) ===");

        System.out.println("\n  Por estado:");
        gestor.estadisticasPorEstado()
                .for((estado, cantidad) ->
                             System.out.printf("    %-12s → %d atención(es)%n", estado, cantidad));

        System.out.println("\n  Por especie:");
        gestor.estadisticasPorEspecie()
                .forEach((especie, cantidad) ->
                        System.out.printf("    %-10s → %d atención(es)%n", especie, cantidad));

        System.out.printf("%n  Total registros  : %d%n", gestor.totalRegistros());
        System.out.printf("  Pendientes       : %d%n", gestor.contarPorEstado("PENDIENTE"));
        System.out.printf("  Procesados       : %d%n", gestor.contarPorEstado("PROCESADO"));
        System.out.printf("  Cancelados       : %d%n", gestor.contarPorEstado("CANCELADO"));
        System.out.println("\n  Validaciones con Stream:");
        System.out.println("    ¿Hay pendientes?          → " + gestor.hayPendientes());
        System.out.println("    ¿Todos tienen código?     → " + gestor.todasTienenCodigo());
        System.out.println("    ¿No hay cancelados?       → " + gestor.noHayCancelados());

        System.out.println("\n  Nombres de mascotas (Stream.map):");
        gestor.obtenerNombresMascotas()
                .forEach(nombre -> System.out.println("    · " + nombre));
    }

    private static void verAgrupamientos() {
        System.out.println("=== AGRUPAMIENTOS (Stream + Collectors.groupingBy) ===");

        System.out.println("\n  Agrupado por ESPECIE:");
        gestor.agruparPorEspecie().forEach((especie, lista) -> {
            System.out.println("  Especie: " + especie + " (" + lista.size() + ")");
            lista.forEach(a -> System.out.println("    → " + a));
        });

        System.out.println("\n  Agrupado por ESTADO:");
        gestor.agruparPorEstado().forEach((estado, lista) -> {
            System.out.println("  Estado: " + estado + " (" + lista.size() + ")");
            lista.forEach(a -> System.out.println("    → " + a));
        });
    }

    private static void cancelarAtencion() {
        System.out.println("=== CANCELAR ATENCIÓN PENDIENTE ===");
        System.out.print("Código de atención a cancelar: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        try {
            gestor.cancelarAtencion(codigo);
            System.out.println("✔  Atención cancelada. Pendientes restantes: " + gestor.totalPendientes());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("✘  " + e.getMessage());
        }
    }

    private static void deshacerProcesamiento() {
        System.out.println("=== DESHACER ÚLTIMO PROCESAMIENTO ===");
        try {
            MascotaAtencion devuelta = gestor.deshacerUltimoProcesamiento();
            System.out.println("✔  Atención devuelta a la cola de pendientes:");
            System.out.println("   " + devuelta);
        } catch (IllegalStateException e) {
            System.out.println("✘  " + e.getMessage());
        }
    }

    private static void verCantidades() {
        System.out.println("=== CANTIDAD DE ELEMENTOS ===");
        System.out.println("  List  (registro general)   : " + gestor.totalRegistros());
        System.out.println("  Queue (pendientes)          : " + gestor.totalPendientes());
        System.out.println("  Deque (historial procesados): " + gestor.totalHistorial());
        System.out.println("  Map   (índice de búsqueda)  : " + gestor.totalIndice());
        System.out.println();
        System.out.println("  Conteos por estado (Stream):");
        System.out.println("    PENDIENTE  : " + gestor.contarPorEstado("PENDIENTE"));
        System.out.println("    PROCESADO  : " + gestor.contarPorEstado("PROCESADO"));
        System.out.println("    CANCELADO  : " + gestor.contarPorEstado("CANCELADO"));
    }

    private static void imprimirLista(List<MascotaAtencion> lista, String mensajeVacio) {
        if (lista.isEmpty()) {
            System.out.println("  " + mensajeVacio);
        } else {
            lista.forEach(a -> System.out.println("  " + a));
            System.out.println("  Total: " + lista.size());
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Ingrese un número válido.");
            }
        }
    }

    private static void pausar() {
        System.out.print("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }
}
