package tecnicas;

import combatientes.Combatiente;
import efectosTemporales.EfectoTemporal;
import efectosTemporales.EstadoVeneno;
import efectosTemporales.EstadoRegeneracion;

/**
 * Catálogo con las cinco técnicas base del torneo.
 * <p>
 * Cada técnica es solo su definición (nombre, costo y efecto): no conoce el
 * tipo del combatiente. La diferencia entre tipos surge del polimorfismo de
 * {@link Combatiente}: el daño pasa por {@code modificarDanioProvocado} del
 * ejecutor y {@code recibirDanio} del objetivo, y el costo lo ajusta
 * {@code modificarCostoEnergia}. Los usos se registran en cada combatiente.
 * </p>
 * <p>
 * <b>Justificación de valores</b> (referencias: ataque básico = 50 de daño
 * y gratis; recuperación de 15 de energía por asalto; energía máxima base
 * entre 150 y 230; vida base entre 800 y 1300; máximo 5 usos por técnica):
 * </p>
 * <ul>
 *   <li><b>Corte de los Mil Amaneceres</b> (costo 30, daño 100): el doble
 *       del básico. Es la opción más eficiente (3,3 de daño por energía) y
 *       se recupera en 2 asaltos.</li>
 *   <li><b>Rugido de la Bestia Primordial</b> (costo 70, daño 220): el
 *       mayor daño total, pero menos eficiente (3,1 por energía) y cuesta
 *       casi 5 asaltos de recuperación: es una decisión táctica.</li>
 *   <li><b>Dominio de la Nada</b> (costo 60, daño 80 y quita 40 de energía):
 *       menos daño directo, pero deja al rival sin recursos para sus
 *       técnicas (equivale a casi 3 asaltos de su recuperación).</li>
 *   <li><b>Muro del Cuerpo Dorado</b> (costo 40, +30 de reducción de daño
 *       por golpe): anula más de la mitad de un ataque básico durante todo
 *       el combate, a cambio de un turno sin atacar.</li>
 *   <li><b>Meditación del Inmortal</b> (costo 50, cura 150): recupera entre
 *       el 11% y el 19% de la vida base de un combatiente.</li>
 * </ul>
 * Ningún costo supera la energía máxima más baja (150), por lo que todas
 * son utilizables por cualquier combatiente.
 */
public final class CatalogoTecnicas {

	// ─── Valores de balance ─────────────────────────────────────────────
	private static final int COSTO_CORTE = 30;
	private static final int DANIO_CORTE = 100;

	private static final int COSTO_RUGIDO = 70;
	private static final int DANIO_RUGIDO = 220;

	private static final int COSTO_DOMINIO = 60;
	private static final int DANIO_DOMINIO = 80;
	private static final int ENERGIA_QUITADA_DOMINIO = 40;

	private static final int COSTO_MURO = 40;
	private static final int REDUCCION_MURO = 30;

	private static final int COSTO_MEDITACION = 50;
	private static final int CURA_MEDITACION = 150;

	private static final int COSTO_VENENO = 40;
	private static final int COSTO_REGENERACION = 40;
	// ─── Técnicas ───────────────────────────────────────────────────────
	public static final Tecnica CORTE_DE_LOS_MIL_AMANECERES = new Tecnica(
			"Corte de los Mil Amaneceres", COSTO_CORTE, TipoTecnica.OFENSIVA,
			(ejecutor, objetivo) -> infligirDanio(ejecutor, objetivo, DANIO_CORTE));

	public static final Tecnica RUGIDO_DE_LA_BESTIA_PRIMORDIAL = new Tecnica(
			"Rugido de la Bestia Primordial", COSTO_RUGIDO, TipoTecnica.OFENSIVA,
			(ejecutor, objetivo) -> infligirDanio(ejecutor, objetivo, DANIO_RUGIDO));

	public static final Tecnica DOMINIO_DE_LA_NADA = new Tecnica(
			"Dominio de la Nada", COSTO_DOMINIO, TipoTecnica.OFENSIVA,
			(ejecutor, objetivo) -> {
				infligirDanio(ejecutor, objetivo, DANIO_DOMINIO);
				objetivo.gastarEnergia(ENERGIA_QUITADA_DOMINIO);
			});

	public static final Tecnica MURO_DEL_CUERPO_DORADO = new Tecnica(
			"Muro del Cuerpo Dorado", COSTO_MURO, TipoTecnica.DEFENSIVA,
			(ejecutor, objetivo) -> ejecutor.agregarReduccionTemporal(REDUCCION_MURO));

	public static final Tecnica MEDITACION_DEL_INMORTAL = new Tecnica(
			"Meditación del Inmortal", COSTO_MEDITACION, TipoTecnica.RECUPERACION,
			(ejecutor, objetivo) -> ejecutor.curar(CURA_MEDITACION));
	
	public static final Tecnica VENENO = new Tecnica(
	        "Veneno", COSTO_VENENO, TipoTecnica.OFENSIVA,
	        (ejecutor, objetivo) -> objetivo.agregarEfecto(
	                new EfectoTemporal(objetivo, new EstadoVeneno())));

	public static final Tecnica REGENERACION = new Tecnica(
	        "Regeneración", COSTO_REGENERACION, TipoTecnica.RECUPERACION,
	        (ejecutor, objetivo) -> ejecutor.agregarEfecto(
	                new EfectoTemporal(ejecutor, new EstadoRegeneracion())));
	private CatalogoTecnicas() {
		// Clase de utilidad: no se instancia.
	}

	/**
	 * Aplica daño ofensivo: el daño base más el bonus del equipamiento del
	 * ejecutor, modificado por su tipo (polimorfismo), y luego modificado por
	 * el tipo del objetivo al recibirlo. Igual que el ataque básico.
	 */
	private static void infligirDanio(Combatiente ejecutor, Combatiente objetivo, int danioBase) {
		int danio = ejecutor.modificarDanioProvocado(danioBase + ejecutor.getBonusDmgTotal());
		objetivo.recibirDanio(danio);
	}
}