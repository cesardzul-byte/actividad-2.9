import java.util.Arrays;
import java.util.NoSuchElementException;

public class Main {

    public static void main(String[] args) {
        SistemaReservas sistema = new SistemaReservas();
        Funcion funcion = new Funcion("Interestelar", 3, "20:30", 12.0);

        // Tecnica usada: Preserve whole object
        // Refactor: Al aplicar esta tecnica los clientes se
        // crean aqui y se pasan completos, en lugar de mandar nombre, email y socio sueltos
        Cliente ana = new Cliente("Ana", "ana@mail.com", true);
        Cliente luis = new Cliente("Luis", "luis@mail.com", false);

        // Tecnica usada: Introduce parameter object / Rename method / Remove parameter
        // Refactor: Al aplicar esta tecnica ahora se llama a
        // reservar(...) (antes hacer), los asientos van como objetos Asiento en vez de dos
        // arreglos paralelos y ya no se pasa el "false" del parámetro imprimir
        // Tecnica usada: Replace error code with exception
        // Refactor: Al aplicar esta tecnica ahora el error se
        // maneja con try/catch y se muestra un mensaje en lugar de un numero negativo
        try {
            int idAna = sistema.reservar(ana, funcion,
                    Arrays.asList(new Asiento('F', 5), new Asiento('F', 6)), "App");
            System.out.println("Reserva Ana: " + idAna);
        } catch (IllegalStateException e) {
            System.out.println("Reserva Ana: ERROR - " + e.getMessage());
        }
        try {
            int idLuis = sistema.reservar(luis, funcion,
                    Arrays.asList(new Asiento('F', 7), new Asiento('F', 6)), "Taquilla");
            System.out.println("Reserva Luis: " + idLuis);
        } catch (IllegalStateException e) {
            System.out.println("Reserva Luis: ERROR - " + e.getMessage());
        }

        // Tecnica usada: Hide method
        // Refactor: Al aplicar esta tecnica ahora ya no se lee la
        // lista pública f.ocupados; se usa la vista de solo lectura getAsientosOcupados()
        System.out.println("Ocupados: " + funcion.getAsientosOcupados());

        // TTecnica usada: Encapsulate downcast
        // Refactor: Al aplicar esta tecnica ahora buscar() devuelve
        // Reserva, asi que desaparece el cast (Reserva) que antes tenia que hacer Main
        try {
            Reserva reservaAna = sistema.buscar(1);
            System.out.println("Precio Ana (socio): " + reservaAna.getPrecio());
        } catch (NoSuchElementException e) {
            System.out.println("Precio Ana (socio): ERROR - " + e.getMessage());
        }

        cancelarYMostrar(sistema, 1);
        cancelarYMostrar(sistema, 99);
    }

    // Tecnica usada: Replace error code with exception
    // Refactor: Al aplicar esta tecnica ahora cancelar() el éxito se imprime como OK y los fallos llegan como excepcione
    // NoSuchElementException (no existe) o IllegalStateException (ya estaba cancelada).
    private static void cancelarYMostrar(SistemaReservas sistema, int id) {
        try {
            sistema.cancelar(id);
            System.out.println("Cancelar " + id + ": OK");
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("Cancelar " + id + ": ERROR - " + e.getMessage());
        }
    }
}