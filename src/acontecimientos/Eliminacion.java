package acontecimientos;

/**
 * Un combatiente queda fuera de combate.
 */
public class Eliminacion extends Acontecimiento {

	private final int asalto;
	private final String combatiente;

	public Eliminacion(int asalto, String combatiente) {
		this.asalto = asalto;
		this.combatiente = combatiente;
	}

	public int getAsalto() {
		return asalto;
	}

	public String getCombatiente() {
		return combatiente;
	}

	@Override
	public String getDescripcion() {
		return "Asalto " + asalto + ": " + combatiente + " queda fuera de combate";
	}
}
