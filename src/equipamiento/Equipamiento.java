package equipamiento;

public class Equipamiento {
    private final String nombre;
    private final Categoria categoria;

    protected Equipamiento(String nombre, Categoria categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public int bonusDanio() { return 0; }
    public int reduccionDanio() { return 0; }
    public int bonusEnergiaMaxima() { return 0; }
    public int bonusVidaMaxima() { return 0; }

    public String getNombre() { return nombre; }
    public Categoria getCategoria() { return categoria; }
}
