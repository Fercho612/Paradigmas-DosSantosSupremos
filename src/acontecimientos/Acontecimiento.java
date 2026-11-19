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

	/**
	 * @return el texto que describe el acontecimiento
	 */
	public abstract String getDescripcion();

	/**
	 * Permite que el Cronista registre los datos que necesita para la tabla
	 * de posiciones. Por defecto no hace nada; lo sobrescriben los
	 * acontecimientos que aportan datos a la tabla.
	 *
	 * @param cronista el cronista que registra el acontecimiento
	 */
	public void registrarEn(CronistaDeLaSecta cronista) {
	}

	@Override
	public String toString() {
		return getDescripcion();
	}
}
