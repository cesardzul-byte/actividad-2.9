import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Lógica de negocio: crea, busca y cancela reservas.
 *
 * NcalcularPrecio(), descuentoDel15() y descuentoDel25() ya no están en esta clase se trasladaron a Reserva
 */
public class SistemaReservas {

    // Tecnica usada: Encapsulate downcast
    // Refactor: el mapa "db" era Map<Integer, Object>, lo que obligaba a castearen cancelar() y en Main ahora es
    // Map<Integer, Reserva> el tipo correcto queda encapsulado en esta clase y el cast desaparece
    // los campos pasaron de public a private y se renombraron variables
    private final Map<Integer, Reserva> reservas = new HashMap<>();
    private int siguienteId = 1;

    // Tecnica usada: Rename method
    // Refactor: hacer() se renombró a reservar() "
    // Tecnica usada: Remove parameter
    // Refactor: se eliminó boolean imprimir, que nunca se leia. Obligaba a los llamadores a inventar un valor y confundia sobre lo que hace el metodo
    // Tecnica usada: Preserve whole object
    // Refactor: ahora se recibe el objeto Cliente completo, si Cliente gana un dato nuevo, esta firma no cambia.
    // Tecnica usada: Introduce parameter object
    // Refactor: char[] filas e int[] numeros  se reemplazaron por List<Asiento> Ya no pueden tener tamaños distintos, asi que el error -2 por esa causa desaparece
    // Tecnica usada: Add parameter
    // Refactor: canal ya no se ignora se pasa al nuevo parametro del constructor de Reserva
    // Tecnica usada: Replace error code with exception
    // Refactor: Ahora solo devuelve ids válidos y los fallos se comunican con excepciones estandar de Java
    /*
     * Crea una reserva de forma atomica, se ocupan todos los asientos solicitados o ninguno.
     *
     * @return id de la reserva creada
     * @throws NullPointerException     si cliente, funcion o canal son null
     * @throws IllegalArgumentException si no se piden asientos o hay asientos repetidos
     * @throws IllegalStateException    si alguno de los asientos ya está ocupado
     */
    public int reservar(Cliente cliente, Funcion funcion, List<Asiento> asientos, String canal) {
        validarSolicitud(cliente, funcion, asientos, canal);
        verificarDisponibilidad(funcion, asientos);
        ocuparAsientos(funcion, asientos);

        Reserva reserva = new Reserva(siguienteId, cliente, funcion, asientos, canal);
        reservas.put(reserva.getId(), reserva);
        siguienteId++;
        return reserva.getId();
    }

    // Tecnica usada: Replace error code with exception
    // Refactor: cancelar devolvía 0 (éxito), -1 (no existe) o -2 (ya cancelada). Ahora es void, si la reserva no existe,
    // buscar() lanza NoSuchElementException y si ya estaba cancelada, se lanza IllegalStateException.
    // Tecnica usada: Encapsulate downcast
    // Refactor: ya no hace (Reserva) db.get(id), reutiliza buscar(id), que devuelve directamente una Reserva.
    // Tecnica usada: Remove setting method
    // Refactor: en lugar de recorrer r.funcion.ocupados y llamar a setCancelada(true), se delega en reserva.cancelar(), que
    // libera los asientos y marca la cancelación.
    /**
     * Cancela una reserva existente y libera sus asientos.
     *
     * @throws NoSuchElementException si no existe una reserva con ese id
     * @throws IllegalStateException  si la reserva ya estaba cancelada
     */
    public void cancelar(int id) {
        Reserva reserva = buscar(id);
        if (reserva.estaCancelada()) {
            throw new IllegalStateException("La reserva " + id + " ya fue cancelada");
        }
        reserva.cancelar();
    }
    /* 
    // Tecnica usada: Encapsulate downcast
    // Refactor: buscar devolvía Object y cada llamador tenía que castear (Reserva) ahora devuelve Reserva
    // Tecnica usada: Replace error code with exception
    // Refactor: antes devolvía null si el id no existía ahora lanza NoSuchElementException
     * @throws NoSuchElementException si no existe una reserva con ese id
     */
    public Reserva buscar(int id) {
        Reserva reserva = reservas.get(id);
        if (reserva == null) {
            throw new NoSuchElementException("No existe la reserva con id " + id);
        }
        return reserva;
    }

    // Tecnica usada: Hide method
    // Refactor: los pasos internos de reservar() se extrajeron a métodos private ninguna otra clase los necesita, asi que no forman parte de la interfaz publica
    private void validarSolicitud(Cliente cliente, Funcion funcion, List<Asiento> asientos, String canal) {
        Objects.requireNonNull(cliente, "El cliente es obligatorio");
        Objects.requireNonNull(funcion, "La función es obligatoria");
        Objects.requireNonNull(canal, "El canal de venta es obligatorio");
        if (asientos == null || asientos.isEmpty()) {
            throw new IllegalArgumentException("Debe solicitarse al menos un asiento");
        }
        if (new HashSet<>(asientos).size() != asientos.size()) {
            throw new IllegalArgumentException("La solicitud contiene asientos repetidos");
        }
    }

    // Tecnica usada: Replace exception with test
    // Refactor: antes se intentaba ocupar cada asiento dentro de try/catch(IllegalStateException) y la excepcion decidía el flujo
    // Si fallaba a mitad, los asientos anteriores quedaban ocupados sin reserva (bug: F7 de Luis).
    // Ahora se compuebqa con funcion.estaDisponible() que todos los asientos estén libres antes de
    // modificar nada, y solo entonces se ocupan
    private void verificarDisponibilidad(Funcion funcion, List<Asiento> asientos) {
        for (Asiento asiento : asientos) {
            if (!funcion.estaDisponible(asiento)) {
                throw new IllegalStateException("Asiento no disponible: " + asiento);
            }
        }
    }

    private void ocuparAsientos(Funcion funcion, List<Asiento> asientos) {
        for (Asiento asiento : asientos) {
            funcion.ocupar(asiento);
        }
    }
}