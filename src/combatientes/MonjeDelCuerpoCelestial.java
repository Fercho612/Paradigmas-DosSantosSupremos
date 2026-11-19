package combatientes;

/**
 * Monje del Cuerpo Celestial: especialista en resistencia.
 * Recibe un 30% menos de daño de todos los ataques.
 * Vida base: 1300 | Energía base: 180
 */
public class MonjeDelCuerpoCelestial extends Combatiente {

	public MonjeDelCuerpoCelestial(int identificador, String nombre) {
		super(identificador, nombre, 1300, 120);
	}

	/**
	 * Reduce el daño recibido en un 30% (mínimo 1).
	 */
	@Override
	public int modificarDanioRecibido(int danio) {
		return Math.max(1, (int) (danio * 0.7));
	}
}
