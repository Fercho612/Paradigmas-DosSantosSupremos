package escuelas;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import equipamiento.Categoria;
import equipamiento.Equipamiento;
import equipamiento.EquipamientoInvalidoException;
import tecnicas.Tecnica;

/**
 * Una escuela de cultivación: nombre, técnicas iniciales y equipamiento
 * inicial. No tiene subclases: agregar una escuela es crear otro objeto.
 * <p>
 * Valida su equipamiento al crearse, con las mismas reglas que el
 * combatiente (máximo 3 objetos, sin dos de la misma categoría). Así una
 * escuela mal armada falla al construirse y no en pleno torneo.
 * </p>
 * <p>
 * Las técnicas se guardan en una lista porque el orden importa: en un
 * empate de usos restantes, el combate elige la primera según el orden de
 * la escuela.
 * </p>
 */
public final class Escuela {

	private static final int MAX_EQUIPAMIENTOS = 3;

	private final String nombre;
	private final List<Tecnica> tecnicas;
	private final List<Equipamiento> equipamiento;

	/**
	 * @param nombre nombre de la escuela (no vacío)
	 * @param tecnicas técnicas iniciales, en orden de preferencia
	 * @param equipamiento equipamiento inicial
	 * @throws EquipamientoInvalidoException si hay más de 3 objetos o dos
	 *         de la misma categoría
	 */
	public Escuela(String nombre, List<Tecnica> tecnicas, List<Equipamiento> equipamiento) {
		Objects.requireNonNull(nombre, "El nombre de la escuela no puede ser null");
		if (nombre.isBlank()) {
			throw new IllegalArgumentException("El nombre de la escuela no puede estar vacío");
		}
		Objects.requireNonNull(tecnicas, "Las técnicas no pueden ser null");
		Objects.requireNonNull(equipamiento, "El equipamiento no puede ser null");

		validarEquipamiento(equipamiento);

		this.nombre = nombre;
		this.tecnicas = List.copyOf(tecnicas);
		this.equipamiento = List.copyOf(equipamiento);
	}

	private static void validarEquipamiento(List<Equipamiento> equipamiento) {
		if (equipamiento.size() > MAX_EQUIPAMIENTOS) {
			throw new EquipamientoInvalidoException(
					"Una escuela no puede asignar más de " + MAX_EQUIPAMIENTOS + " objetos");
		}
		List<Categoria> vistas = new ArrayList<>();
		for (Equipamiento e : equipamiento) {
			if (vistas.contains(e.getCategoria())) {
				throw new EquipamientoInvalidoException(
						"La escuela repite la categoría " + e.getCategoria());
			}
			vistas.add(e.getCategoria());
		}
	}

	public String getNombre() {
		return nombre;
	}

	/** Técnicas iniciales (lista no modificable, en orden de la escuela). */
	public List<Tecnica> getTecnicas() {
		return tecnicas;
	}

	/** Equipamiento inicial (lista no modificable). */
	public List<Equipamiento> getEquipamiento() {
		return equipamiento;
	}

	@Override
	public String toString() {
		return nombre;
	}
}