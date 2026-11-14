package equipamiento;

public abstract class Equipamiento {
    private final String nombre;
    private final Categoria categoria;

    protected Equipamiento(String nombre, Categoria categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public int bonusDmg() { return 0; }
    public int reduccionDmg() { return 0; }
    public int bonusEnergiaMax() { return 0; }
    public int bonusVidaMax() { return 0; }

    public String getNombre() { return nombre; }
    public Categoria getCategoria() { return categoria; }
}
