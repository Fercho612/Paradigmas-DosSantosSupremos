package combatientes;

/**
 * Cultivador de la Espada: especialista en daño ofensivo.
 * Inflige un 50% más de daño con todos sus ataques.
 * Vida base: 1000 | Energía base: 150
 */
public class CultivadorDeLaEspada extends Combatiente {

	public CultivadorDeLaEspada(int identificador, String nombre) {
		super(identificador, nombre, 1000, 150);
	}

	/**
	 * Amplifica el daño provocado en un 50%.
	 */
	@Override
	public int modificarDanioProvocado(int danioBase) {
		return (int) (danioBase * 1.5);
	}
}
