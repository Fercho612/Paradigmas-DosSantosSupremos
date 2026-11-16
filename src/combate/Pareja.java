package combate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import combatientes.Combatiente;
import tecnicas.Tecnica;

/**
 * Representa una pareja de combatientes en el torneo.
 * Cada pareja tiene exactamente dos integrantes, ordenados por identificador.
 * Gestiona la "regla del compañero": cuando un integrante usa una técnica,
 * su compañero no puede usar esa misma técnica mientras el primero aún tenga
 * usos restantes y siga en pie.
 */
public class Pareja {

	private final int identificador;
	private final Combatiente integrante1; // el de menor ID
	private final Combatiente integrante2; // el de mayor ID
	private Map<Tecnica, Combatiente> tecnicasBloqueadas;

	/**
	 * Crea una nueva pareja asignándole un identificador único.
	 * Los integrantes se almacenan ordenados: el de menor ID como integrante1.
	 *
	 * @param identificador identificador único de la pareja
	 * @param integrante1   uno de los dos combatientes
	 * @param integrante2   el otro combatiente
	 */
	public Pareja(int identificador, Combatiente integrante1, Combatiente integrante2) {
		this.identificador = identificador;

		// Ordenar por identificador: el menor como integrante1
		if (integrante1.getIdentificador() <= integrante2.getIdentificador()) {
			this.integrante1 = integrante1;
			this.integrante2 = integrante2;
		} else {
			this.integrante1 = integrante2;
			this.integrante2 = integrante1;
		}

		this.tecnicasBloqueadas = new HashMap<>();
	}

	// ======================== Getters básicos ========================

	public int getIdentificador() {
		return identificador;
	}

	public Combatiente getIntegrante1() {
		return integrante1;
	}

	public Combatiente getIntegrante2() {
		return integrante2;
	}

	/**
	 * Devuelve una lista inmutable con ambos integrantes, ordenados por ID.
	 *
	 * @return lista con [integrante1, integrante2]
	 */
	public List<Combatiente> getIntegrantes() {
		return List.of(integrante1, integrante2);
	}

	// ======================== Estado de la pareja ========================

	/**
	 * Una pareja está eliminada si ambos integrantes están fuera de combate.
	 *
	 * @return true si ambos cayeron
	 */
	public boolean estaEliminada() {
		return integrante1.estaFueraDeCombate() && integrante2.estaFueraDeCombate();
	}

	/**
	 * Retorna la suma de la vida actual de ambos integrantes.
	 * Un integrante caído aporta 0.
	 *
	 * @return vida total de la pareja
	 */
	public int getVidaTotal() {
		return integrante1.getVidaActual() + integrante2.getVidaActual();
	}

	/**
	 * Devuelve los integrantes que siguen en pie (no están fuera de combate),
	 * ordenados por identificador.
	 *
	 * @return lista de integrantes activos
	 */
	public List<Combatiente> getIntegrantesEnPie() {
		List<Combatiente> enPie = new ArrayList<>();
		// Ya están ordenados por ID gracias al constructor
		if (!integrante1.estaFueraDeCombate()) {
			enPie.add(integrante1);
		}
		if (!integrante2.estaFueraDeCombate()) {
			enPie.add(integrante2);
		}
		return enPie;
	}

	// ======================== Gestión de combate ========================

	/**
	 * Restaura a ambos integrantes para un nuevo combate y limpia
	 * las reglas del compañero.
	 */
	public void restaurarParaCombate() {
		integrante1.restaurarParaCombate();
		integrante2.restaurarParaCombate();
		tecnicasBloqueadas.clear();
	}

	/**
	 * Verifica si un combatiente de esta pareja puede usar una técnica,
	 * considerando la regla del compañero:
	 * <ul>
	 *   <li>Si la técnica fue usada por el compañero y este aún tiene usos
	 *       restantes y sigue en pie, el combatiente NO puede usarla.</li>
	 *   <li>Si el compañero cayó o agotó los usos, la técnica queda liberada.</li>
	 * </ul>
	 * Además verifica que el combatiente tenga energía y usos disponibles.
	 *
	 * @param combatiente el integrante que quiere usar la técnica
	 * @param tecnica     la técnica a usar
	 * @return true si puede usarla
	 */
	public boolean puedeUsarTecnica(Combatiente combatiente, Tecnica tecnica) {
		// Primero verificar la regla del compañero
		Combatiente bloqueador = tecnicasBloqueadas.get(tecnica);
		if (bloqueador != null && !bloqueador.equals(combatiente)) {
			// La técnica fue registrada por el compañero
			// Solo está bloqueada si el compañero aún tiene usos Y sigue en pie
			if (bloqueador.getUsosRestantes(tecnica) > 0 && !bloqueador.estaFueraDeCombate()) {
				return false;
			}
		}

		// Verificar que el combatiente pueda usar la técnica (energía y usos)
		return combatiente.puedeUsarTecnica(tecnica);
	}

	/**
	 * Registra que un combatiente usó una técnica, activando la regla del compañero
	 * para esa técnica. También delega al combatiente el registro del uso.
	 *
	 * @param combatiente el integrante que usó la técnica
	 * @param tecnica     la técnica utilizada
	 */
	public void registrarUsoTecnica(Combatiente combatiente, Tecnica tecnica) {
		tecnicasBloqueadas.put(tecnica, combatiente);
		combatiente.registrarUsoTecnica(tecnica);
	}

	/**
	 * Limpia las reglas del compañero. Se invoca al inicio de cada combate.
	 */
	public void limpiarReglasCompanero() {
		tecnicasBloqueadas.clear();
	}

	// ======================== Consultas de integrantes ========================

	/**
	 * Verifica si un combatiente pertenece a esta pareja.
	 *
	 * @param c el combatiente a buscar
	 * @return true si es integrante1 o integrante2
	 */
	public boolean contiene(Combatiente c) {
		return integrante1.equals(c) || integrante2.equals(c);
	}

	/**
	 * Devuelve el compañero del combatiente dado.
	 *
	 * @param c uno de los integrantes
	 * @return el otro integrante de la pareja
	 * @throws IllegalArgumentException si el combatiente no pertenece a esta pareja
	 */
	public Combatiente getCompanero(Combatiente c) {
		if (integrante1.equals(c)) {
			return integrante2;
		}
		if (integrante2.equals(c)) {
			return integrante1;
		}
		throw new IllegalArgumentException(
				"El combatiente '" + c.getNombre() + "' no pertenece a esta pareja");
	}

	// ======================== equals, hashCode, toString ========================

	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(identificador));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!(obj instanceof Pareja))
			return false;

		Pareja other = (Pareja) obj;
		return identificador == other.identificador;
	}

	@Override
	public String toString() {
		return "Pareja{id=" + identificador
				+ ", integrante1='" + integrante1.getNombre()
				+ "', integrante2='" + integrante2.getNombre() + "'}";
	}
}
