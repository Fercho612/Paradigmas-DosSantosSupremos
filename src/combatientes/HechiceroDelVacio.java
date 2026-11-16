package combatientes;

/**
 * Hechicero del Vacío: especialista en eficiencia energética.
 * Gasta un 30% menos de energía al usar técnicas.
 * Vida base: 800 | Energía base: 230
 */
public class HechiceroDelVacio extends Combatiente {

	public HechiceroDelVacio(int identificador, String nombre) {
		super(identificador, nombre, 800, 230);
	}

	/**
	 * Reduce el costo de energía en un 30% (mínimo 1).
	 */
	@Override
	public int modificarCostoEnergia(int costoBase) {
		return Math.max(1, (int) (costoBase * 0.7));
	}
}
