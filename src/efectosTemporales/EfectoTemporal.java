package efectosTemporales;

import combatientes.Combatiente;

public class EfectoTemporal {

    private EstadoEfecto estado;
    private Combatiente combatiente;

    public EfectoTemporal(Combatiente combatiente, EstadoEfecto estado) {
        this.combatiente = combatiente;
        this.estado = estado;
    }

    public Combatiente getCombatiente() {
        return combatiente;
    }

    public void aplicar() {
        estado.aplicar(this);
    }

    public void finalizar() {
        estado = new EstadoInactivo();
    }
    public boolean estaActivo() {
        return estado.estaActivo();
    }
}