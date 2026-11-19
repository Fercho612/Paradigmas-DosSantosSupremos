package acontecimientos;

/**
 * Hecho del combate o del torneo que se comunica a los interesados.
 * <p>
 * Cada acontecimiento guarda los datos del momento en que ocurrió (nombres,
 * identificadores y valores), no referencias a los combatientes, que siguen
 * cambiando después.
 * </p>
 */
public abstract class Acontecimiento {

	public abstract String getDescripcion();

	public void registrarEn(CronistaDeLaSecta cronista) {
	}

	@Override
	public String toString() {
		return getDescripcion();
	}
}
