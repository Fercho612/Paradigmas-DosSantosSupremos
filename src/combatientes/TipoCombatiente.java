package combatientes;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Tipos de combatiente disponibles. Cada valor sabe construir su propia
 * clase concreta, de modo que quien necesite un combatiente (el creador) no
 * necesita conocer la clase ni usar {@code if/else} ni {@code instanceof}.
 * <p>
 * Para agregar un tipo nuevo basta con crear su subclase de
 * {@link Combatiente} y sumar un valor a este enum; el resto del sistema no
 * se modifica.
 * </p>
 */
public enum TipoCombatiente {

	CULTIVADOR_DE_LA_ESPADA(CultivadorDeLaEspada::new),
	MONJE_DEL_CUERPO_CELESTIAL(MonjeDelCuerpoCelestial::new),
	HECHICERO_DEL_VACIO(HechiceroDelVacio::new);

	private final BiFunction<Integer, String, Combatiente> constructor;

	TipoCombatiente(BiFunction<Integer, String, Combatiente> constructor) {
		this.constructor = constructor;
	}

	/**
	 * Construye un combatiente de este tipo, sin equipamiento ni técnicas.
	 *
	 * @param identificador identificador único del combatiente
	 * @param nombre nombre del combatiente
	 * @return el combatiente nuevo
	 */
	public Combatiente crear(int identificador, String nombre) {
		Objects.requireNonNull(nombre, "El nombre no puede ser null");
		return constructor.apply(identificador, nombre);
	}
}