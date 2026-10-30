import java.util.List;

public class Reserva {
    public int id;
    public Cliente cliente;
    public Funcion funcion;
    public List<String> asientos;
    public double precio;
    public boolean cancelada;

    public Reserva(int id, Cliente cliente, Funcion funcion, List<String> asientos) {
        this.id = id;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asientos = asientos;
    }

    public void setCancelada(boolean cancelada) {
        this.cancelada = cancelada;
    }
}