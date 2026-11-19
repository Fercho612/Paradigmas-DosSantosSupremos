package acontecimientos;

/**
 * Fila de la tabla de posiciones: la última ronda que jugó una pareja y la
 * vida total con la que terminó su último combate.
 */
public class Posicion {

	private final int idPareja;
	private final int ultimaRonda;
	private final int vidaTotalFinal;

	public Posicion(int idPareja, int ultimaRonda, int vidaTotalFinal) {
		this.idPareja = idPareja;
		this.ultimaRonda = ultimaRonda;
		this.vidaTotalFinal = vidaTotalFinal;
	}

	public int getIdPareja() {
		return idPareja;
	}

	public int getUltimaRonda() {
		return ultimaRonda;
	}

	public int getVidaTotalFinal() {
		return vidaTotalFinal;
	}

	@Override
	public String toString() {
		return "Pareja " + idPareja + " - última ronda: " + ultimaRonda
				+ " - vida total final: " + vidaTotalFinal;
	}
}
