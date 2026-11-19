package combatientes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import equipamiento.Equipamiento;
import equipamiento.EquipamientoInvalidoException;
import tecnicas.Tecnica;
import efectosTemporales.EfectoTemporal;

/**
 * Clase abstracta base para todos los combatientes del torneo.
 * Gestiona vida, energía, equipamiento, técnicas y mecánicas de combate.
 */
public abstract class Combatiente {
	private final int identificador;
	private final String nombre;
	private final int vidaMax, energiaMax;
	private int vidaAct, energiaAct;
	private final List<Equipamiento> equipamiento = new ArrayList<>();
	private static final int MAX_EQUIPAMIENTOS = 3;

	// --- Campos para el sistema de combate (F3) ---
	private List<Tecnica> tecnicas = new ArrayList<>();
	private Map<Tecnica, Integer> usosTecnicas = new HashMap<>();
	private boolean fueraDeCombate = false;
	private int reduccionDmgTemporal = 0;
	private List<EfectoTemporal> efectosTemporales = new ArrayList<>();

	private static final int MAX_USOS_POR_TECNICA = 5;
	private static final int ENERGIA_POR_ASALTO = 15;
	private static final int DANIO_BASICO = 50;

	protected Combatiente(int identificador, String nombre, int vidaMax, int energiaMax) {
		this.identificador = identificador;
		this.nombre = nombre;
		this.vidaMax = vidaMax;
		this.vidaAct = vidaMax;
		this.energiaMax = energiaMax;
		this.energiaAct = energiaMax;
	}

	// ==================== Equipamiento (existente) ====================

	public void equipar(Equipamiento objeto) {
		if (this.equipamiento.size() >= MAX_EQUIPAMIENTOS) {
			throw new EquipamientoInvalidoException("No puede llevar más de " + MAX_EQUIPAMIENTOS + " objetos");
		}
		for (Equipamiento e : this.equipamiento) {
			if (e.getCategoria() == objeto.getCategoria()) {
				throw new EquipamientoInvalidoException("Ya lleva un objeto de categoría " + objeto.getCategoria());
			}
		}
		this.equipamiento.add(objeto);
	}

	public void quitar(Equipamiento objeto) {
		if (!this.equipamiento.remove(objeto)) {
			throw new EquipamientoInvalidoException("No lleva " + objeto.getNombre());
		}
		this.vidaAct = Math.min(vidaAct, getVidaMaxima());
		this.energiaAct = Math.min(energiaAct, getEnergiaMaxima());
	}

	public List<Equipamiento> getEquipamiento() {
		return List.copyOf(equipamiento);
	}

	public int getVidaMaxima() {
		int total = this.vidaMax;
		for (Equipamiento e : this.equipamiento) {
			total += e.bonusVidaMax();
		}
		return total;
	}

	public int getEnergiaMaxima() {
		int total = this.energiaMax;
		for (Equipamiento e : this.equipamiento) {
			total += e.bonusEnergiaMax();
		}
		return total;
	}

	// ==================== Getters básicos ====================

	public int getIdentificador() {
		return identificador;
	}

	public String getNombre() {
		return nombre;
	}

	public int getVidaActual() {
		return vidaAct;
	}

	public int getEnergiaActual() {
		return energiaAct;
	}

	// ==================== Técnicas ====================

	/**
	 * Establece las técnicas disponibles para este combatiente.
	 * Inicializa el contador de usos de cada técnica a 0.
	 */
	public void setTecnicas(List<Tecnica> tecnicas) {
		this.tecnicas = new ArrayList<>(tecnicas);
		for (Tecnica t : tecnicas) {
			this.usosTecnicas.put(t, Integer.valueOf(0));
		}
	}

	/** Devuelve una copia no modificable de las técnicas disponibles. */
	public List<Tecnica> getTecnicas() {
		return List.copyOf(tecnicas);
	}

	/**
	 * Devuelve cuántos usos le quedan a la técnica en el torneo actual.
	 * Máximo {@value #MAX_USOS_POR_TECNICA} usos por técnica por torneo.
	 */
	public int getUsosRestantes(Tecnica t) {
		return MAX_USOS_POR_TECNICA - usosTecnicas.getOrDefault(t, Integer.valueOf(0)).intValue();
	}

	/** Registra un uso de la técnica en el mapa de usos del torneo. */
	public void registrarUsoTecnica(Tecnica t) {
		usosTecnicas.put(t, Integer.valueOf(usosTecnicas.getOrDefault(t, Integer.valueOf(0)).intValue() + 1));
	}

	/**
	 * Verifica si el combatiente puede usar la técnica:
	 * tiene suficiente energía (con modificador de clase) y usos restantes.
	 */
	public boolean puedeUsarTecnica(Tecnica t) {
		if (!tecnicas.contains(t)) {
			return false;
		}
		int costoReal = modificarCostoEnergia(t.getCostoEnergia());
		return energiaAct >= costoReal && getUsosRestantes(t) > 0;
	}
	// ==================== Estado de combate ====================

	public boolean estaFueraDeCombate() {
		return fueraDeCombate;
	}

	// ==================== Daño y reducción ====================

	/**
	 * El combatiente recibe daño. Se aplica:
	 * 1. Modificador de clase (ej: MonjeDelCuerpoCelestial reduce 30%)
	 * 2. Reducción total (equipamiento + temporal de técnicas defensivas)
	 * El daño final mínimo es 0. Si la vida llega a 0, queda fuera de combate.
	 * @return el daño efectivamente infligido
	 */
	public int recibirDanio(int danio) {
		int danioModificado = modificarDanioRecibido(danio);
		int danioFinal = Math.max(0, danioModificado - getReduccionDmgTotal());
		this.vidaAct -= danioFinal;
		if (this.vidaAct <= 0) {
			this.vidaAct = 0;
			this.fueraDeCombate = true;
		}
		return danioFinal;
	}

	/** Suma de reducción de daño de equipamiento + reducción temporal de defensas. */
	public int getReduccionDmgTotal() {
		int total = reduccionDmgTemporal;
		for (Equipamiento e : this.equipamiento) {
			total += e.reduccionDmg();
		}
		return total;
	}

	/** Suma de bonus de daño de todo el equipamiento. */
	public int getBonusDmgTotal() {
		int total = 0;
		for (Equipamiento e : this.equipamiento) {
			total += e.bonusDmg();
		}
		return total;
	}

	/** Agrega reducción de daño temporal (usada por técnicas defensivas). */
	public void agregarReduccionTemporal(int cantidad) {
		this.reduccionDmgTemporal += cantidad;
	}

	/** Limpia la reducción de daño temporal (al inicio de cada combate). */
	public void limpiarReduccionTemporal() {
		this.reduccionDmgTemporal = 0;
	}

	/** Indica si el combatiente tiene alguna técnica defensiva activa en este combate. */
	public boolean tieneDefensaActiva() {
		return reduccionDmgTemporal > 0;
	}

	/**
	 * Cura al combatiente la cantidad indicada de vida, sin superar el máximo.
	 * @param cantidad puntos de vida a recuperar
	 */
	public void curar(int cantidad) {
		this.vidaAct = Math.min(this.vidaAct + cantidad, getVidaMaxima());
	}
	public void agregarEfecto(EfectoTemporal efecto) {
    efectosTemporales.add(efecto);
}

public void aplicarEfectosTemporales() {
    for (EfectoTemporal efecto : efectosTemporales) {
        efecto.aplicar();
    }
    efectosTemporales.clear();
}

	// ==================== Energía ====================

	public void gastarEnergia(int cantidad) {
		this.energiaAct = Math.max(0, this.energiaAct - cantidad);
	}

	/** Recupera {@value #ENERGIA_POR_ASALTO} puntos de energía (sin exceder el máximo). */
	public void recuperarEnergia() {
		this.energiaAct = Math.min(this.energiaAct + ENERGIA_POR_ASALTO, getEnergiaMaxima());
	}

	// ==================== Restauración / Reinicio ====================

	/**
	 * Restaura al combatiente para un nuevo combate:
	 * vida y energía al máximo, no fuera de combate, sin reducción temporal.
	 * NO resetea los usos de técnicas (persisten durante el torneo).
	 */
	public void restaurarParaCombate() {
		this.vidaAct = getVidaMaxima();
		this.energiaAct = getEnergiaMaxima();
		this.fueraDeCombate = false;
		this.reduccionDmgTemporal = 0;
	}

	/**
	 * Reinicia los usos de técnicas para un nuevo torneo.
	 */
	public void reiniciarParaTorneo() {
		for (Tecnica t : usosTecnicas.keySet()) {
			usosTecnicas.put(t, Integer.valueOf(0));
		}
	}

	// ==================== Acciones de combate ====================

	/**
	 * Ejecuta un ataque básico contra el objetivo.
	 * Daño = (DANIO_BASICO + bonusDmgEquipamiento) modificado por clase.
	 * @return el daño efectivamente infligido al objetivo
	 */
	public int ejecutarAtaqueBasico(Combatiente objetivo) {
		int danioBase = DANIO_BASICO + getBonusDmgTotal();
		int danioModificado = modificarDanioProvocado(danioBase);
		return objetivo.recibirDanio(danioModificado);
	}

	public void ejecutarTecnica(Tecnica t, Combatiente objetivo) {
		if (!puedeUsarTecnica(t)) {
			throw new IllegalStateException(
					nombre + " no puede ejecutar la técnica '" + t.getNombre()
					+ "' (sin energía, sin usos o no la conoce)");
		}
		int costoReal = modificarCostoEnergia(t.getCostoEnergia());
		gastarEnergia(costoReal);
		registrarUsoTecnica(t);
		t.ejecutar(this, objetivo);
	}

	// ==================== Getters estáticos ====================

	/** Devuelve el daño base de un ataque básico. */
	public static int getDanioBasico() {
		return DANIO_BASICO;
	}

	/** Devuelve la energía recuperada por asalto. */
	public static int getEnergiaPorAsalto() {
		return ENERGIA_POR_ASALTO;
	}

	// ==================== Polimorfismo de clase ====================

	/**
	 * Modifica el daño provocado por este combatiente.
	 * Las subclases ofensivas pueden amplificarlo.
	 */
	public int modificarDanioProvocado(int danioBase) {
		return danioBase;
	}

	/**
	 * Modifica el daño recibido por este combatiente.
	 * Las subclases defensivas pueden reducirlo.
	 */
	public int modificarDanioRecibido(int danio) {
		return danio;
	}

	/**
	 * Modifica el costo de energía de una técnica para este combatiente.
	 * Las subclases mágicas pueden abaratarlo.
	 */
	public int modificarCostoEnergia(int costoBase) {
		return costoBase;
	}

	// ==================== Object ====================

	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(identificador));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof Combatiente)) return false;
		Combatiente other = (Combatiente) obj;
		return identificador == other.identificador;
	}

	@Override
	public String toString() {
		return "Combatiente{id=" + identificador + ", nombre='" + nombre
				+ "', vida=" + vidaAct + "/" + getVidaMaxima()
				+ ", energia=" + energiaAct + "/" + getEnergiaMaxima() + "}";
	}
}
