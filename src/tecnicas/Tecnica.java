package tecnicas;

import java.util.Objects;

import combatientes.Combatiente;

public class Tecnica {
    private final String nombre;
    private final int costoEnergia;
    private final TipoTecnica tipo;
    private final Efecto efecto;

    public Tecnica(final String nombre, final int costoEnergia, final TipoTecnica tipo, final Efecto efecto) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre de la técnica no puede ser null");
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

    /** Dos técnicas son iguales si tienen el mismo nombre. */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tecnica otra = (Tecnica) obj;
        return nombre.equals(otra.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
