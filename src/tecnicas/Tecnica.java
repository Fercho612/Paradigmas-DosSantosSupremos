package tecnicas;

import combatientes.Combatiente;

public class Tecnica {
    private final String nombre;
    private final int costoEnergia;
    private final TipoTecnica tipo;
    private final Efecto efecto;

    public Tecnica(final String nombre, final int costoEnergia, final TipoTecnica tipo, final Efecto efecto) {
        this.nombre = nombre;
        this.costoEnergia = costoEnergia;
        this.tipo = tipo;
        this.efecto = efecto;
    }

    public String getNombre() { return nombre; }
    public int getCostoEnergia() { return costoEnergia; }
    public TipoTecnica getTipo() { return tipo; }

    public void ejecutar(Combatiente ejecutor, Combatiente objetivo) {
        // Ejecuta la estrategia inyectada dinámicamente
        this.efecto.aplicar(ejecutor, objetivo);
    }
}
