package combatientes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import escuelas.CatalogoEscuelas;
import tecnicas.CatalogoTecnicas;

class CreadorCombatientesTest {

	@Test
	void losIdentificadoresSeAsignanEnOrdenDeCreacion() {
		CreadorCombatientes creador = new CreadorCombatientes();

		Combatiente a = creador.crear(TipoCombatiente.HECHICERO_DEL_VACIO,
				CatalogoEscuelas.PALACIO_DEL_VACIO, "A");
		Combatiente b = creador.crear(TipoCombatiente.MONJE_DEL_CUERPO_CELESTIAL,
				CatalogoEscuelas.MONASTERIO_DEL_CUERPO_DORADO, "B");
		Combatiente c = creador.crear(TipoCombatiente.CULTIVADOR_DE_LA_ESPADA,
				CatalogoEscuelas.ESPADA_CELESTIAL, "C");

		assertEquals(1, a.getIdentificador());
		assertEquals(2, b.getIdentificador());
		assertEquals(3, c.getIdentificador());
	}

	@Test
	void cadaCreadorNumeraDesdeUno() {
		Combatiente deUno = new CreadorCombatientes().crear(
				TipoCombatiente.HECHICERO_DEL_VACIO, CatalogoEscuelas.PALACIO_DEL_VACIO, "X");
		Combatiente deOtro = new CreadorCombatientes().crear(
				TipoCombatiente.HECHICERO_DEL_VACIO, CatalogoEscuelas.PALACIO_DEL_VACIO, "X");

		assertEquals(1, deUno.getIdentificador());
		assertEquals(1, deOtro.getIdentificador());
	}

	@Test
	void creaElTipoPedido() {
		CreadorCombatientes creador = new CreadorCombatientes();

		assertEquals(HechiceroDelVacio.class, creador.crear(TipoCombatiente.HECHICERO_DEL_VACIO,
				CatalogoEscuelas.PALACIO_DEL_VACIO, "H").getClass());
		assertEquals(MonjeDelCuerpoCelestial.class, creador.crear(TipoCombatiente.MONJE_DEL_CUERPO_CELESTIAL,
				CatalogoEscuelas.PALACIO_DEL_VACIO, "M").getClass());
		assertEquals(CultivadorDeLaEspada.class, creador.crear(TipoCombatiente.CULTIVADOR_DE_LA_ESPADA,
				CatalogoEscuelas.PALACIO_DEL_VACIO, "C").getClass());
	}

	@Test
	void laEscuelaPreparaAlCombatienteConSuEquipamientoYTecnicas() {
		Combatiente hechicero = new CreadorCombatientes().crear(
				TipoCombatiente.HECHICERO_DEL_VACIO, CatalogoEscuelas.PALACIO_DEL_VACIO, "H");

		assertEquals(CatalogoEscuelas.PALACIO_DEL_VACIO.getTecnicas(), hechicero.getTecnicas());
		assertEquals(1, hechicero.getEquipamiento().size());
		assertEquals(230 + 50, hechicero.getEnergiaMaxima()); // 230 base + Anillo de Doran
		assertEquals(230, hechicero.getEnergiaActual());      // equipar no sube el valor actual
	}

	@Test
	void equiparAlCrearNoSubeLaVidaActual() {
		Combatiente monje = new CreadorCombatientes().crear(
				TipoCombatiente.MONJE_DEL_CUERPO_CELESTIAL,
				CatalogoEscuelas.MONASTERIO_DEL_CUERPO_DORADO, "M");

		assertEquals(1300 + 300, monje.getVidaMaxima()); // 1300 base + Corazón de Acero
		assertEquals(1300, monje.getVidaActual());
	}

	@Test
	void cualquierTipoPuedeCombinarseConCualquierEscuela() {
		Combatiente monjeDeLaEspada = new CreadorCombatientes().crear(
				TipoCombatiente.MONJE_DEL_CUERPO_CELESTIAL, CatalogoEscuelas.ESPADA_CELESTIAL, "M");

		assertEquals(MonjeDelCuerpoCelestial.class, monjeDeLaEspada.getClass());
		assertEquals(CatalogoTecnicas.CORTE_DE_LOS_MIL_AMANECERES, monjeDeLaEspada.getTecnicas().get(0));
	}

	@Test
	void rechazaArgumentosNulos() {
		CreadorCombatientes creador = new CreadorCombatientes();

		assertThrows(NullPointerException.class,
				() -> creador.crear(null, CatalogoEscuelas.PALACIO_DEL_VACIO, "X"));
		assertThrows(NullPointerException.class,
				() -> creador.crear(TipoCombatiente.HECHICERO_DEL_VACIO, null, "X"));
		assertThrows(NullPointerException.class,
				() -> creador.crear(TipoCombatiente.HECHICERO_DEL_VACIO,
						CatalogoEscuelas.PALACIO_DEL_VACIO, null));
	}
}