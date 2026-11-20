package efectosTemporales;

import combatientes.Combatiente;

/**
 * Efecto temporal sobre un combatiente (patron State: el comportamiento lo
 * define su {@link EstadoEfecto}).
 * <p>
 * Reglas del bonus de efectos temporales:
 * </p>
 * <ul>
 *   <li><b>Cuando se aplica:</b> al comienzo del proximo turno del combatiente
 *       afectado, antes de que elija su accion.</li>
 *   <li><b>Cuanto dura:</b> una sola aplicacion. {@link EstadoVeneno} le quita
 *       40 de vida (la reducen las defensas y el equipamiento, como cualquier
 *       dano) y {@link EstadoRegeneracion} le cura 40 (sin pasar su maximo).</li>
 *   <li><b>Que sucede al terminar:</b> el efecto pasa a {@link EstadoInactivo},
 *       se descarta y no deja ninguna consecuencia.</li>
 *   <li><b>Acumulacion:</b> mientras el combatiente tenga un efecto activo,
 *       un efecto nuevo se ignora (los efectos no se acumulan).</li>
 *   <li>Si el veneno deja al combatiente fuera de combate, no actua en ese
 *       turno.</li>
 * </ul>
 */
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