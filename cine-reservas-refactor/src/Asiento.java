import java.util.Objects;

// Tecnica usada: Introduce parameter object
// Refactor: Al aplicar esta tecnica se realizaron los siguientes cambios: se creo la clase Asiento
// para agrupar fila y numero, antes viajaban en dos arreglos paralelos (char[] filas, int[] numeros)
// y como dos parametros sueltos en Funcion.ocupar(char, int). Es inmutable, define equals/hashCode
// para compararse y guardarse en colecciones, y toString() conserva el formato 
public final class Asiento {

    private final char fila;
    private final int numero;

    public Asiento(char fila, int numero) {
        if (!Character.isLetter(fila)) {
            throw new IllegalArgumentException("La fila debe ser una letra: " + fila);
        }
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de asiento debe ser positivo: " + numero);
        }
        this.fila = fila;
        this.numero = numero;
    }

    public char getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof Asiento)) {
            return false;
        }
        Asiento otroAsiento = (Asiento) otro;
        return fila == otroAsiento.fila && numero == otroAsiento.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fila, numero);
    }

    @Override
    public String toString() {
        return String.valueOf(fila) + numero;
    }
}