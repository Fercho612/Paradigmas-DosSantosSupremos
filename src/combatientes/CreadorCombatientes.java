package combatientes;

import java.util.Objects;

import equipamiento.Equipamiento;
import escuelas.Escuela;

/**
 * Factory simple de combatientes. Centraliza la creación: quien pide un
 * combatiente indica solo el <b>tipo</b>, la <b>escuela</b> que lo prepara y
 * su nombre; no necesita conocer la clase concreta, sus atributos iniciales
 * ni cómo se arma su preparación.
 * <p>
 * Los identificadores se asignan en orden de creación (1, 2, 3...). El
 * contador es de instancia: cada creador numera desde 1, lo que mantiene
 * independientes (y reproducibles) a dos torneos armados por separado.
 * </p>
 */
public class CreadorCombatientes {

	private int proximoIdentificador = 1;

	/**
	 * Crea un combatiente del tipo indicado, preparado por la escuela: con
	 * su equipamiento y sus técnicas iniciales.
	 *
	 * @param tipo tipo de combatiente
	 * @param escuela escuela que lo prepara
	 * @param nombre nombre del combatiente
	 * @return el combatiente listo para inscribirse
	 */
	public Combatiente crear(TipoCombatiente tipo, Escuela escuela, String nombre) {
		Objects.requireNonNull(tipo, "El tipo no puede ser null");
		Objects.requireNonNull(escuela, "La escuela no puede ser null");
		Objects.requireNonNull(nombre, "El nombre no puede ser null");

		Combatiente combatiente = tipo.crear(proximoIdentificador, nombre);

		for (Equipamiento objeto : escuela.getEquipamiento()) {
			combatiente.equipar(objeto);
		}
		combatiente.setTecnicas(escuela.getTecnicas());

		// El identificador se consume solo si la creación fue exitosa,
		// para que no queden "huecos" en la numeración.
		proximoIdentificador++;
		return combatiente;
	}
}