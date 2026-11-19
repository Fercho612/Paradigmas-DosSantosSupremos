package acontecimientos;

/**
 * Inicio de un combate entre dos parejas.
 */
public class InicioCombate extends Acontecimiento {

	private final int ronda;
	private final int idPareja1;
	private final String nombresPareja1;
	private final int idPareja2;
	private final String nombresPareja2;

	public InicioCombate(int ronda, int idPareja1, String nombresPareja1,
			int idPareja2, String nombresPareja2) {
		this.ronda = ronda;
		this.idPareja1 = idPareja1;
		this.nombresPareja1 = nombresPareja1;
		this.idPareja2 = idPareja2;
		this.nombresPareja2 = nombresPareja2;
	}

	public int getRonda() {
		return ronda;
	}

	public int getIdPareja1() {
		return idPareja1;
	}

	public int getIdPareja2() {
		return idPareja2;
	}

	@Override
	public String getDescripcion() {
		return "Ronda " + ronda + ": comienza el combate entre la pareja " + idPareja1
				+ " (" + nombresPareja1 + ") y la pareja " + idPareja2
				+ " (" + nombresPareja2 + ")";
	}
}
