package acontecimientos;

/**
 * Proclamación de la pareja campeona como los Dos Santos Supremos.
 */
public class Proclamacion extends Acontecimiento {

	private final int idPareja;
	private final String integrante1;
	private final String integrante2;

	public Proclamacion(int idPareja, String integrante1, String integrante2) {
		this.idPareja = idPareja;
		this.integrante1 = integrante1;
		this.integrante2 = integrante2;
	}

	public int getIdPareja() {
		return idPareja;
	}

	@Override
	public String getDescripcion() {
		return integrante1 + " y " + integrante2 + " (pareja " + idPareja
				+ ") son proclamados los Dos Santos Supremos";
	}
}
