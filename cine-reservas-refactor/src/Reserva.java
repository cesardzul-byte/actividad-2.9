import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Reserva de uno o mas asientos de una funcion para un cliente.
 */
public class Reserva {

    // el numero magico 0.15 de descuentoDel15 pasa a una constante con nombre.
    private static final double DESCUENTO_SOCIO = 0.15;

    // Ahora todos los campos son private. Todos son final salvo
    // "cancelada", que solo cambia mediante cancelar(). El precio ya no se asigna desde fuera
    private final int id;
    private final Cliente cliente;
    private final Funcion funcion;
    private final List<Asiento> asientos;
    private final String canal;
    private final double precio;
    private boolean cancelada;

    // Tecnica usada: Add parameter
    // Refactor: Al aplicar esta tecnica se agregó el parametro canal, además antes hacer() recibía "App" o "Taquilla" y lo
    // descartaba, ahora la reserva registra por que canal se vendió.
    // Tecnica usada: Introduce parameter object
    // Refactor: Al aplicar esta tecnica ahora los asientos llegan como List<Asiento> en vez de List<String>, además, se guarda una copia inmodificable para que nadie
    // altere la reserva desde fuera.
    public Reserva(int id, Cliente cliente, Funcion funcion, List<Asiento> asientos, String canal) {
        this.id = id;
        this.cliente = Objects.requireNonNull(cliente, "El cliente es obligatorio");
        this.funcion = Objects.requireNonNull(funcion, "La función es obligatoria");
        this.asientos = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(asientos, "Los asientos son obligatorios")));
        this.canal = Objects.requireNonNull(canal, "El canal es obligatorio");
        this.precio = calcularPrecio();
    }

    // Tecnica usada: Replace parameter with method y Mo9ve Method
    // REFACTOR: antes era SistemaReservas.calcularPrecio(Reserva r, String tipo) y el llamador calculaba
    // tipo = socio ? "socio" : "general", algo que la reserva ya conoce (cliente.esSocio())
    // Se elimino el parámetro tipo y el metodo lo obtiene por si mismo. Como todo lo que usa
    // pertenece a la reserva, se movió a Reserva, así que también desapareció el parámetro r.
    // El precio se calcula una sola vez, en el constructor.
    // Tecnica usada: Hide method
    // REFACTOR: Al aplicar esta técnica ahora calcularPrecio,
    // precioGeneral, precioSocio y aplicarDescuento son private. Antes calcularPrecio y los descuentos eran public aunque nadie de fuera los usaba
    private double calcularPrecio() {
        double base = funcion.getPrecio() * asientos.size();
        // Tecnica usada: Replace parameter with explicit methods
        // Refactor: el texto "general" o "socio" y el if/else con equals() se reemplazaron por dos metodos explicitos
        // Desaparece el riesgo de un texto mal escrito 
        return cliente.esSocio() ? precioSocio(base) : precioGeneral(base);
    }

    private double precioGeneral(double base) {
        return base;
    }

    private double precioSocio(double base) {
        return aplicarDescuento(base, DESCUENTO_SOCIO);
    }

    // Tecnica usada: Parameterize method
    // Refactor: descuentoDel15(p) y descuentoDel25(p) eran lo mismo excepto por el porcentaje. Se reemplazaron por
    // aplicarDescuento(monto, porcentaje). descuentoDel25 no tenía usos, asi que no se le creo
    // constante, si algún día se necesita, basta con aplicarDescuento(monto, 0.25)
    private static double aplicarDescuento(double monto, double porcentaje) {
        return monto - monto * porcentaje;
    }

    // Tecnica usada: Remove setting method
    // Refactor: se elimino setCancelada(boolean), que permitia incluso descancelar una reserva. Lo sustituye
    // cancelar(), que solo permite pasar de activa a cancelada y que además libera sus asientos 
    /* 
     * Cancela la reserva y libera sus asientos
     * Condición: la reserva no puede estar cancelada 
     */
    public void cancelar() {
        if (cancelada) {
            throw new IllegalStateException("La reserva " + id + " ya está cancelada");
        }
        for (Asiento asiento : asientos) {
            funcion.liberar(asiento);
        }
        cancelada = true;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public String getCanal() {
        return canal;
    }

    public double getPrecio() {
        return precio;
    }

    public boolean estaCancelada() {
        return cancelada;
    }
}