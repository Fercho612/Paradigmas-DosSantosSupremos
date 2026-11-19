package acontecimientos;

/**
 * Acción realizada por un combatiente: ataque básico o técnica.
 */
public class AccionRealizada extends Acontecimiento {

	private final int asalto;
	private final String ejecutor;
	private final String accion;
	private final String objetivo;
	private final int vidaObjetivo;

	/**
	 * @param asalto número de asalto
	 * @param ejecutor nombre de quien realiza la acción
	 * @param accion nombre de la acción ("Ataque básico" o la técnica)
	 * @param objetivo nombre de quien recibe la acción
	 * @param vidaObjetivo vida del objetivo después de la acción
	 */
	public AccionRealizada(int asalto, String ejecutor, String accion,
			String objetivo, int vidaObjetivo) {
		this.asalto = asalto;
		this.ejecutor = ejecutor;
		this.accion = accion;
		this.objetivo = objetivo;
		this.vidaObjetivo = vidaObjetivo;
	}

	public int getAsalto() {
		return asalto;
	}

	public String getEjecutor() {
		return ejecutor;
	}

	public String getAccion() {
		return accion;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public int getVidaObjetivo() {
		return vidaObjetivo;
	}

	@Override
	public String getDescripcion() {
		return "Asalto " + asalto + ": " + ejecutor + " usa " + accion + " sobre "
				+ objetivo + " (vida: " + vidaObjetivo + ")";
	}
}
