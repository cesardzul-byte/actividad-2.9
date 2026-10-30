import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Proyeccion de una pelicula. Es la unica dueña del estado de sus asientos
 */
public class Funcion {

    private final String titulo;
    private final int sala;
    private final String hora;

    /* Tecnica usada: Remove setting method
    // Refactor: Al aplicar esta tecnica se eliminó
    */ setPrecio(double), que no tenia uso. El precio se fija en el constructor y es final cambiarlo después alteraría el cobro de reservas ya hechas
    private final double precio;

    /* Tecnica usada: Hide method
    // Refactor: Al aplicar esta tecnica la lista publica
    // "ocupados" paso a ser privada (asientosOcupados). Solo se modifica con ocupar() y liberar() y se consulta con getAsientosOcupados()
    // Se usa LinkedHashSet porque no admite duplicados y conserva el ordenen el que se insertan,
    / asa que la salida se sigue imprimiendo como [F5, F6].
    */
    private final Set<Asiento> asientosOcupados = new LinkedHashSet<>();

    public Funcion(String titulo, int sala, String hora, double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo: " + precio);
        }
        this.titulo = Objects.requireNonNull(titulo, "El título es obligatorio");
        this.sala = sala;
        this.hora = Objects.requireNonNull(hora, "La hora es obligatoria");
        this.precio = precio;
    }

    /* Tecnica usada: Separate query from modifier
    // Refactor: Antes de aplicar esta tecnica ocupar(fila, numero)
    // comprobaba, modificaba y lanzaba excepción a la vez, ahora se separo en estaDisponible(asiento),
    // una consulta sin efectos secundarios que puede llamarse cuantas veces se quiera y ocupar(asiento) que solo modifica el estado
    */
    /** indica si el asiento está libre */
    public boolean estaDisponible(Asiento asiento) {
        return !asientosOcupados.contains(asiento);
    }

    /* Tecnica usada: Introduce parameter object
    // Refactor: Al aplicar esta tecnica ocupar(char fila,
    // int numero) ahora recibe un Asiento; el código "F5" ya no se arma a mano con "" + fila + numero.
    */
    /**
     * Modificador: marca el asiento como ocupado.
     * Esta conidicionado a que estaDisponible(asiento) sea true, de lo contrario lanza IllegalStateException.
     */
    public void ocupar(Asiento asiento) {
        if (!estaDisponible(asiento)) {
            throw new IllegalStateException("Precondición violada: el asiento " + asiento + " ya está ocupado");
        }
        asientosOcupados.add(asiento);
    }

    /** Modificador: libera un asiento. Sustituye a r.funcion.ocupados.remove(a) de SistemaReservas. */
    public void liberar(Asiento asiento) {
        asientosOcupados.remove(asiento);
    }

    public Set<Asiento> getAsientosOcupados() {
        return Collections.unmodifiableSet(asientosOcupados);
    }

    public String getTitulo() {
        return titulo;
    }

    public int getSala() {
        return sala;
    }

    public String getHora() {
        return hora;
    }

    public double getPrecio() {
        return precio;
    }
}