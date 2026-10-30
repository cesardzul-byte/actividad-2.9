import java.util.ArrayList;
import java.util.List;

public class Funcion {
    public String titulo;
    public int sala;
    public String hora;
    public double precio;
    public List<String> ocupados = new ArrayList<>();

    public Funcion(String titulo, int sala, String hora, double precio) {
        this.titulo = titulo;
        this.sala = sala;
        this.hora = hora;
        this.precio = precio;
    }

    // Ocupa el asiento y lanza excepción si ya estaba ocupado
    public void ocupar(char fila, int numero) {
        String codigo = "" + fila + numero;
        if (ocupados.contains(codigo)) {
            throw new IllegalStateException("Asiento ocupado: " + codigo);
        }
        ocupados.add(codigo);
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
