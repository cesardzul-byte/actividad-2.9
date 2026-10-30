import java.util.Objects;

/**
 * Datos de la persona que hace una reserva. Es inmutable.
 */
public class Cliente {

    // Se realiza encapsulamiento, los campos pasaron de public a private final
    // Solo se exponen para lectura, de esa forma ninguna otra clase puede alterar los datos del cliente
    private final String nombre;
    private final String email;
    private final boolean socio;

    // Tecnica usada: Replace error code with exception
    // Refactor: Al aplicar esta tecnica  la regla
    // "si nombre == null devolver -1" que estaba en SistemaReservas.hacer() se trasladó aquí y
    // ahora lanza una excepción con mensaje claro. Esto hace que ya no puede existir un Cliente sin nombre.
    public Cliente(String nombre, String email, boolean socio) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre del cliente es obligatorio");
        this.email = email;
        this.socio = socio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public boolean esSocio() {
        return socio;
    }
}