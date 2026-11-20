package combate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import acontecimientos.Acontecimiento;
import acontecimientos.AccionRealizada;
import acontecimientos.Eliminacion;
import acontecimientos.InicioCombate;
import acontecimientos.Interesado;
import acontecimientos.ResultadoCombate;
import combatientes.Combatiente;
import tecnicas.Tecnica;
import tecnicas.TipoTecnica;


/**
 * Representa un combate entre dos {@link Pareja}s dentro del sistema de torneo.
 * <p>
 * El combate se divide en asaltos (rondas). En cada asalto, cada combatiente
 * en pie de ambas parejas ejecuta una acción. El combate es completamente
 * <b>determinístico</b>: dado el mismo estado inicial, siempre produce el mismo
 * resultado.
 * </p>
 * <p>
 * La pareja con menor identificador actúa primero en cada asalto. Si se
 * alcanza el límite de asaltos sin que ninguna pareja sea eliminada, gana la
 * que tenga más vida total; en empate, la de menor identificador.
 * </p>
 */
public class Combate {

	// ─── Constantes ─────────────────────────────────────────────────────
	/** Cantidad máxima de asaltos antes de resolver por vida total. */
	private static final int LIMITE_ASALTOS = 30;


	// ─── Campos ─────────────────────────────────────────────────────────
	/** Pareja con menor identificador (actúa primera). */
	private final Pareja pareja1;
	/** Pareja con mayor identificador (actúa segunda). */
	private final Pareja pareja2;

	/** Pareja ganadora del combate ({@code null} si aún no finalizó). */
	private Pareja ganadora;

	/** Número del asalto en curso (1-indexado). */
	private int asaltoActual;

	/** Indica si el combate ya fue ejecutado y finalizado. */
	private boolean finalizado;

	private final int ronda;
	private final List<Interesado> interesados;

	// ─── Clase interna: Acción ──────────────────────────────────────────
	/**
	 * Representa la acción que un combatiente ejecutará en su turno.
	 * Si {@code tecnica} es {@code null}, se trata de un ataque básico.
	 */
	private static class Accion {
		final Tecnica tecnica;

		Accion(Tecnica tecnica) {
			this.tecnica = tecnica;
		}

		boolean esAtaqueBasico() {
			return tecnica == null;
		}
	}

	// ─── Constructor ────────────────────────────────────────────────────
	/**
	 * Crea un nuevo combate entre dos parejas.
	 * <p>
	 * La pareja con menor identificador se asigna como {@code pareja1}.
	 * Ambas parejas se restauran para combate (vida, energía, estado de
	 * fuera-de-combate y reducción temporal se reinician; los usos de
	 * técnicas <b>no</b> se reinician).
	 * </p>
	 *
	 * @param p1 primera pareja
	 * @param p2 segunda pareja
	 */
	public Combate(Pareja p1, Pareja p2) {
		this(p1, p2, 0, new ArrayList<>());
	}

	public Combate(Pareja p1, Pareja p2, int ronda, List<Interesado> interesados) {
		// Ordenar por identificador: la menor va primero
		if (p1.getIdentificador() <= p2.getIdentificador()) {
			this.pareja1 = p1;
			this.pareja2 = p2;
		} else {
			this.pareja1 = p2;
			this.pareja2 = p1;
		}

		// Restaurar combatientes para el inicio del combate
		this.pareja1.restaurarParaCombate();
		this.pareja2.restaurarParaCombate();

		this.ganadora = null;
		this.asaltoActual = 0;
		this.finalizado = false;
		this.ronda = ronda;
		this.interesados = interesados;
	}

	// ─── Método principal ───────────────────────────────────────────────
	/**
	 * Ejecuta el combate completo y devuelve la pareja ganadora.
	 * <p>
	 * Algoritmo:
	 * <ol>
	 *   <li>Por cada asalto (hasta {@value #LIMITE_ASALTOS}):</li>
	 *   <ul>
	 *     <li>Cada combatiente en pie recupera energía (orden: pareja1
	 *         primero, luego pareja2; dentro de cada pareja, por ID).</li>
	 *     <li>Turno de pareja1 (atacante) contra pareja2 (defensora).</li>
	 *     <li>Si pareja2 fue eliminada, gana pareja1.</li>
	 *     <li>Turno de pareja2 (atacante) contra pareja1 (defensora).</li>
	 *     <li>Si pareja1 fue eliminada, gana pareja2.</li>
	 *   </ul>
	 *   <li>Si se agota el límite, gana la pareja con más vida total;
	 *       en empate, la de menor identificador (pareja1).</li>
	 * </ol>
	 *
	 * @return la {@link Pareja} ganadora
	 */
	public Pareja ejecutar() {
		notificar(new InicioCombate(ronda,
				pareja1.getIdentificador(), nombresDe(pareja1),
				pareja2.getIdentificador(), nombresDe(pareja2)));

		for (asaltoActual = 1; asaltoActual <= LIMITE_ASALTOS; asaltoActual++) {

			// ── Fase de recuperación de energía ──
			recuperarEnergiaEnPie(pareja1);
			recuperarEnergiaEnPie(pareja2);

			// ── Turno de pareja1 ──
			ejecutarTurno(pareja1, pareja2);
			if (pareja2.estaEliminada()) {
				return finalizar(pareja1);
			}

			// ── Turno de pareja2 ──
			ejecutarTurno(pareja2, pareja1);
			if (pareja1.estaEliminada()) {
				return finalizar(pareja2);
			}
		}

		// ── Límite de asaltos alcanzado: desempate por vida total ──
		if (pareja1.getVidaTotal() >= pareja2.getVidaTotal()) {
			return finalizar(pareja1); // En empate de vida, gana la de menor ID
		}
		return finalizar(pareja2);
	}

	private Pareja finalizar(Pareja ganadora) {
		this.ganadora = ganadora;
		this.finalizado = true;
		Pareja perdedora = getPerdedora();
		notificar(new ResultadoCombate(ronda,
				ganadora.getIdentificador(), ganadora.getVidaTotal(),
				perdedora.getIdentificador(), perdedora.getVidaTotal()));
		return ganadora;
	}

	// ─── Lógica de turno ────────────────────────────────────────────────
	/**
	 * Ejecuta el turno de una pareja atacante contra una pareja defensora.
	 * Cada combatiente en pie de la pareja atacante (ordenados por ID
	 * ascendente) elige y ejecuta una acción. Si la pareja defensora es
	 * eliminada durante el turno, los combatientes restantes no actúan.
	 *
	 * @param atacante pareja que ataca en este turno
	 * @param defensora pareja que defiende en este turno
	 */
	private void ejecutarTurno(Pareja atacante, Pareja defensora) {
    List<Combatiente> enPie = atacante.getIntegrantesEnPie();

    for (Combatiente combatiente : enPie) {
        if (defensora.estaEliminada()) {
            break;
        }

        combatiente.aplicarEfectosTemporales();

        // Si el veneno lo dejó fuera de combate, no actúa.
        if (combatiente.estaFueraDeCombate()) {
    notificar(new Eliminacion(asaltoActual, combatiente.getNombre()));
    continue;
}

        Accion accion = elegirAccion(combatiente, atacante, defensora);
        ejecutarAccion(combatiente, atacante, accion, defensora);
    }
}

	// ─── Selección de acción (IA determinística) ────────────────────────
	/**
	 * Determina la acción que realizará un combatiente según la siguiente
	 * prioridad:
	 * <ol>
	 *   <li>Técnica de RECUPERACIÓN, si le falta vida
	 *       ({@code vidaActual < vidaMaxima}).</li>
	 *   <li>Técnica DEFENSIVA, si no tiene una defensa activa
	 *       ({@code tieneDefensaActiva()} devuelve {@code false}).</li>
	 *   <li>Técnica OFENSIVA con más usos restantes; en empate de usos,
	 *       la primera en el orden de la lista de técnicas.</li>
	 *   <li>Ataque básico, si ninguna técnica es utilizable.</li>
	 * </ol>
	 * Para cada técnica considerada se verifica:
	 * <ul>
	 *   <li>{@code combatiente.puedeUsarTecnica(t)} — energía y usos
	 *       restantes.</li>
	 *   <li>{@code parejaPropia.puedeUsarTecnica(combatiente, t)} — regla
	 *       del compañero.</li>
	 * </ul>
	 *
	 * @param combatiente el combatiente que actúa
	 * @param parejaPropia la pareja a la que pertenece
	 * @param parejaRival la pareja contraria
	 * @return la {@link Accion} elegida
	 */
	private Accion elegirAccion(Combatiente combatiente, Pareja parejaPropia,
								Pareja parejaRival) {

		List<Tecnica> tecnicas = combatiente.getTecnicas();

		// 1) Intentar técnica de RECUPERACIÓN si le falta vida
		if (combatiente.getVidaActual() < combatiente.getVidaMaxima()) {
			Tecnica recuperacion = buscarTecnicaUsable(combatiente, parejaPropia,
					tecnicas, TipoTecnica.RECUPERACION);
			if (recuperacion != null) {
				return new Accion(recuperacion);
			}
		}

		// 2) Intentar técnica DEFENSIVA si no tiene defensa activa
		if (!combatiente.tieneDefensaActiva()) {
			Tecnica defensiva = buscarTecnicaUsable(combatiente, parejaPropia,
					tecnicas, TipoTecnica.DEFENSIVA);
			if (defensiva != null) {
				return new Accion(defensiva);
			}
		}

		// 3) Intentar técnica OFENSIVA con más usos restantes
		Tecnica ofensiva = buscarMejorOfensiva(combatiente, parejaPropia, tecnicas);
		if (ofensiva != null) {
			return new Accion(ofensiva);
		}

		// 4) Ataque básico por defecto
		return new Accion(null);
	}

	/**
	 * Busca la primera técnica utilizable del tipo indicado en la lista de
	 * técnicas del combatiente (respetando el orden original).
	 *
	 * @param combatiente el combatiente
	 * @param parejaPropia su pareja
	 * @param tecnicas lista de técnicas del combatiente
	 * @param tipo tipo de técnica buscado
	 * @return la técnica encontrada, o {@code null} si ninguna es utilizable
	 */
	private Tecnica buscarTecnicaUsable(Combatiente combatiente, Pareja parejaPropia,
										List<Tecnica> tecnicas, TipoTecnica tipo) {
		for (Tecnica t : tecnicas) {
			if (t.getTipo() == tipo
					&& combatiente.puedeUsarTecnica(t)
					&& parejaPropia.puedeUsarTecnica(combatiente, t)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Busca la técnica ofensiva utilizable con mayor cantidad de usos
	 * restantes. En caso de empate, se elige la que aparece primero en la
	 * lista de técnicas (orden de la escuela).
	 *
	 * @param combatiente el combatiente
	 * @param parejaPropia su pareja
	 * @param tecnicas lista de técnicas del combatiente
	 * @return la mejor técnica ofensiva, o {@code null} si no hay ninguna
	 */
	private Tecnica buscarMejorOfensiva(Combatiente combatiente, Pareja parejaPropia,
										List<Tecnica> tecnicas) {
		Tecnica mejor = null;
		int mejorUsos = -1;

		for (Tecnica t : tecnicas) {
			if (t.getTipo() == TipoTecnica.OFENSIVA
					&& combatiente.puedeUsarTecnica(t)
					&& parejaPropia.puedeUsarTecnica(combatiente, t)) {
				int usos = combatiente.getUsosRestantes(t);
				if (usos > mejorUsos) {
					mejor = t;
					mejorUsos = usos;
				}
				// En empate de usos, se queda con la primera encontrada
				// (respeta el orden original de la lista)
			}
		}
		return mejor;
	}

	// ─── Ejecución de acción ────────────────────────────────────────────
	/**
	 * Ejecuta la acción elegida por un combatiente.
	 * <ul>
	 *   <li><b>Ataque básico:</b> elige objetivo rival con menor vida (menor
	 *       ID en empate) y ejecuta {@code ejecutarAtaqueBasico}.</li>
	 *   <li><b>Técnica ofensiva:</b> gasta energía, registra uso, elige
	 *       objetivo rival y ejecuta la técnica sobre él.</li>
	 *   <li><b>Técnica defensiva/recuperación:</b> gasta energía, registra
	 *       uso y ejecuta la técnica sobre sí mismo.</li>
	 * </ul>
	 *
	 * @param ejecutor el combatiente que realiza la acción
	 * @param parejaPropia la pareja del ejecutor
	 * @param accion la acción a ejecutar
	 * @param parejaRival la pareja contraria
	 */
	private void ejecutarAccion(Combatiente ejecutor, Pareja parejaPropia,
								Accion accion, Pareja parejaRival) {
		if (accion.esAtaqueBasico()) {
			Combatiente objetivo = elegirObjetivo(parejaRival);
			ejecutor.ejecutarAtaqueBasico(objetivo);
			notificarAccion(ejecutor, "Ataque básico", objetivo);
		} else {
			Tecnica t = accion.tecnica;

			// Calcular costo real (puede estar modificado por equipamiento/tipo)
			int costoReal = ejecutor.modificarCostoEnergia(t.getCostoEnergia());
			ejecutor.gastarEnergia(costoReal);

			// Registrar el uso en la pareja (afecta regla del compañero)
			parejaPropia.registrarUsoTecnica(ejecutor, t);

			if (t.getTipo() == TipoTecnica.OFENSIVA) {
				Combatiente objetivo = elegirObjetivo(parejaRival);
				t.ejecutar(ejecutor, objetivo);
				notificarAccion(ejecutor, t.getNombre(), objetivo);
			} else {
				// DEFENSIVA o RECUPERACION: se aplica sobre sí mismo
				t.ejecutar(ejecutor, ejecutor);
				notificarAccion(ejecutor, t.getNombre(), ejecutor);
			}
		}
	}

	private void notificarAccion(Combatiente ejecutor, String nombreAccion, Combatiente objetivo) {
		notificar(new AccionRealizada(asaltoActual, ejecutor.getNombre(), nombreAccion,
				objetivo.getNombre(), objetivo.getVidaActual()));
		if (objetivo.estaFueraDeCombate()) {
			notificar(new Eliminacion(asaltoActual, objetivo.getNombre()));
		}
	}

	private void notificar(Acontecimiento acontecimiento) {
		for (Interesado interesado : interesados) {
			interesado.notificar(acontecimiento);
		}
	}

	private String nombresDe(Pareja pareja) {
		return pareja.getIntegrante1().getNombre() + " y " + pareja.getIntegrante2().getNombre();
	}

	// ─── Selección de objetivo ──────────────────────────────────────────
	/**
	 * Elige el objetivo de un ataque ofensivo de entre los combatientes en
	 * pie de la pareja rival.
	 * <p>
	 * Criterio: el rival con <b>menor vida actual</b>; en empate, el de
	 * <b>menor identificador</b>.
	 * </p>
	 *
	 * @param parejaRival la pareja de la cual elegir un objetivo
	 * @return el combatiente elegido como objetivo
	 */
	private Combatiente elegirObjetivo(Pareja parejaRival) {
		return parejaRival.getIntegrantesEnPie().stream()
				.min(Comparator
						.comparingInt(Combatiente::getVidaActual)
						.thenComparingInt(Combatiente::getIdentificador))
				.orElseThrow(() -> new IllegalStateException(
						"No hay combatientes en pie en la pareja rival"));
	}

	// ─── Recuperación de energía ────────────────────────────────────────
	/**
	 * Hace que cada combatiente en pie de la pareja recupere energía al
	 * inicio del asalto.
	 *
	 * @param pareja la pareja cuyos integrantes en pie recuperan energía
	 */
	private void recuperarEnergiaEnPie(Pareja pareja) {
		for (Combatiente c : pareja.getIntegrantesEnPie()) {
			c.recuperarEnergia();
		}
	}

	// ─── Getters públicos ───────────────────────────────────────────────

	/**
	 * @return la pareja ganadora, o {@code null} si el combate no ha finalizado
	 */
	public Pareja getGanadora() {
		return ganadora;
	}

	/**
	 * @return la pareja perdedora, o {@code null} si el combate no ha finalizado
	 * @throws IllegalStateException si el combate no ha finalizado
	 */
	public Pareja getPerdedora() {
		if (!finalizado) {
			throw new IllegalStateException("El combate aún no ha finalizado");
		}
		return ganadora.equals(pareja1) ? pareja2 : pareja1;
	}

	/**
	 * @return el número del asalto actual (o el último jugado si ya finalizó)
	 */
	public int getAsaltoActual() {
		return asaltoActual;
	}

	/**
	 * @return {@code true} si el combate ya fue ejecutado y finalizado
	 */
	public boolean isFinalizado() {
		return finalizado;
	}

	/**
	 * @return la primera pareja (la de menor identificador)
	 */
	public Pareja getPareja1() {
		return pareja1;
	}

	/**
	 * @return la segunda pareja (la de mayor identificador)
	 */
	public Pareja getPareja2() {
		return pareja2;
	}

	/**
	 * @return el límite máximo de asaltos por combate
	 */
	public static int getLimiteAsaltos() {
		return LIMITE_ASALTOS;
	}
}
