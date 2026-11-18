package tecnicas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import combatientes.Combatiente;
import combatientes.CultivadorDeLaEspada;
import combatientes.HechiceroDelVacio;
import combatientes.MonjeDelCuerpoCelestial;

class TecnicaTest {

	private static final Tecnica CORTE = CatalogoTecnicas.CORTE_DE_LOS_MIL_AMANECERES;

	private static int vidaPerdida(Combatiente c) {
		return c.getVidaMaxima() - c.getVidaActual();
	}

	// ─── La misma técnica daña distinto según el tipo ───────────────────

	@Test
	void laMismaTecnicaDaniaDistintoSegunElTipoDelEjecutor() {
		Combatiente cultivador = new CultivadorDeLaEspada(1, "Cultivador");
		Combatiente monje = new MonjeDelCuerpoCelestial(2, "Monje");
		Combatiente victima1 = new HechiceroDelVacio(3, "Victima1");
		Combatiente victima2 = new HechiceroDelVacio(4, "Victima2");
		cultivador.setTecnicas(List.of(CORTE));
		monje.setTecnicas(List.of(CORTE));

		cultivador.ejecutarTecnica(CORTE, victima1);
		monje.ejecutarTecnica(CORTE, victima2);

		assertEquals(150, vidaPerdida(victima1)); // 100 base +50% del Cultivador
		assertEquals(100, vidaPerdida(victima2)); // 100 base, el Monje no lo altera
	}

	@Test
	void laMismaTecnicaDaniaDistintoSegunElTipoDelObjetivo() {
		Combatiente cultivador = new CultivadorDeLaEspada(1, "Cultivador");
		Combatiente monje = new MonjeDelCuerpoCelestial(2, "Monje");
		Combatiente hechicero = new HechiceroDelVacio(3, "Hechicero");
		cultivador.setTecnicas(List.of(CORTE));

		cultivador.ejecutarTecnica(CORTE, monje);
		cultivador.ejecutarTecnica(CORTE, hechicero);

		assertTrue(vidaPerdida(monje) < vidaPerdida(hechicero),
				"El Monje debe recibir menos daño que el Hechicero");
	}

	@Test
	void laMismaTecnicaCuestaDistintoSegunElTipoDelEjecutor() {
		Combatiente cultivador = new CultivadorDeLaEspada(1, "Cultivador");
		Combatiente hechicero = new HechiceroDelVacio(2, "Hechicero");
		Combatiente victima = new MonjeDelCuerpoCelestial(3, "Victima");
		cultivador.setTecnicas(List.of(CORTE));
		hechicero.setTecnicas(List.of(CORTE));

		cultivador.ejecutarTecnica(CORTE, victima);
		hechicero.ejecutarTecnica(CORTE, victima);

		int gastoCultivador = cultivador.getEnergiaMaxima() - cultivador.getEnergiaActual();
		int gastoHechicero = hechicero.getEnergiaMaxima() - hechicero.getEnergiaActual();
		assertEquals(CORTE.getCostoEnergia(), gastoCultivador);
		assertTrue(gastoHechicero < gastoCultivador, "El Hechicero debe gastar menos energía");
	}

	// ─── No se ejecuta sin energía o sin usos ───────────────────────────

	@Test
	void noSeEjecutaSinEnergia() {
		Combatiente ejecutor = new CultivadorDeLaEspada(1, "Ejecutor");
		Combatiente victima = new MonjeDelCuerpoCelestial(2, "Victima");
		ejecutor.setTecnicas(List.of(CORTE));
		ejecutor.gastarEnergia(ejecutor.getEnergiaMaxima() + 100); // se acota a 0

		assertEquals(0, ejecutor.getEnergiaActual());
		assertFalse(ejecutor.puedeUsarTecnica(CORTE));
		assertThrows(IllegalStateException.class, () -> ejecutor.ejecutarTecnica(CORTE, victima));

		// Sin efectos colaterales: no se gastó ni se registró nada.
		assertEquals(5, ejecutor.getUsosRestantes(CORTE));
		assertEquals(0, vidaPerdida(victima));
	}

	@Test
	void noSeEjecutaSinUsosRestantesAunqueHayaEnergia() {
		Combatiente ejecutor = new CultivadorDeLaEspada(1, "Ejecutor");
		Combatiente victima = new MonjeDelCuerpoCelestial(2, "Victima");
		ejecutor.setTecnicas(List.of(CORTE));

		for (int i = 0; i < 5; i++) {
			assertTrue(ejecutor.puedeUsarTecnica(CORTE));
			ejecutor.ejecutarTecnica(CORTE, victima);
			ejecutor.restaurarParaCombate(); // recupera energía, conserva usos
		}

		assertEquals(0, ejecutor.getUsosRestantes(CORTE));
		assertEquals(ejecutor.getEnergiaMaxima(), ejecutor.getEnergiaActual());
		assertFalse(ejecutor.puedeUsarTecnica(CORTE));
		assertThrows(IllegalStateException.class, () -> ejecutor.ejecutarTecnica(CORTE, victima));
	}

	@Test
	void unaTecnicaQueNoConoceNoPuedeUsarse() {
		Combatiente ejecutor = new CultivadorDeLaEspada(1, "Ejecutor");
		ejecutor.setTecnicas(List.of(CORTE));

		assertFalse(ejecutor.puedeUsarTecnica(CatalogoTecnicas.RUGIDO_DE_LA_BESTIA_PRIMORDIAL));
	}

	// ─── Igualdad por nombre ────────────────────────────────────────────

	@Test
	void dosTecnicasConElMismoNombreSonIguales() {
		Tecnica original = new Tecnica("Técnica X", 10, TipoTecnica.OFENSIVA, (e, o) -> { });
		Tecnica copia = new Tecnica("Técnica X", 99, TipoTecnica.DEFENSIVA, (e, o) -> { });
		Tecnica otra = new Tecnica("Técnica Y", 10, TipoTecnica.OFENSIVA, (e, o) -> { });

		assertEquals(original, copia);
		assertEquals(original.hashCode(), copia.hashCode());
		assertNotEquals(original, otra);
	}

	@Test
	void elCombatienteReconoceUnaTecnicaIgualPorNombre() {
		Combatiente ejecutor = new CultivadorDeLaEspada(1, "Ejecutor");
		ejecutor.setTecnicas(List.of(CORTE));
		Tecnica copia = new Tecnica(CORTE.getNombre(), CORTE.getCostoEnergia(),
				TipoTecnica.OFENSIVA, (e, o) -> { });

		assertEquals(5, ejecutor.getUsosRestantes(copia));
		assertTrue(ejecutor.puedeUsarTecnica(copia));
	}

	// ─── Efectos de las demás técnicas ──────────────────────────────────

	@Test
	void dominioDeLaNadaQuitaEnergiaSinBajarDeCero() {
		Combatiente ejecutor = new HechiceroDelVacio(1, "Ejecutor");
		Combatiente victima = new MonjeDelCuerpoCelestial(2, "Victima");
		Tecnica dominio = CatalogoTecnicas.DOMINIO_DE_LA_NADA;
		ejecutor.setTecnicas(List.of(dominio));
		victima.gastarEnergia(victima.getEnergiaMaxima() - 10); // le quedan 10

		ejecutor.ejecutarTecnica(dominio, victima);

		assertEquals(0, victima.getEnergiaActual());
		assertTrue(vidaPerdida(victima) > 0);
	}

	@Test
	void muroDelCuerpoDoradoActivaLaDefensa() {
		Combatiente ejecutor = new MonjeDelCuerpoCelestial(1, "Ejecutor");
		Tecnica muro = CatalogoTecnicas.MURO_DEL_CUERPO_DORADO;
		ejecutor.setTecnicas(List.of(muro));

		assertFalse(ejecutor.tieneDefensaActiva());
		ejecutor.ejecutarTecnica(muro, ejecutor);

		assertTrue(ejecutor.tieneDefensaActiva());
	}

	@Test
	void meditacionDelInmortalCuraSinSuperarElMaximo() {
		Combatiente ejecutor = new MonjeDelCuerpoCelestial(1, "Ejecutor");
		Tecnica meditacion = CatalogoTecnicas.MEDITACION_DEL_INMORTAL;
		ejecutor.setTecnicas(List.of(meditacion));
		ejecutor.recibirDanio(500);
		int vidaTrasDanio = ejecutor.getVidaActual();

		ejecutor.ejecutarTecnica(meditacion, ejecutor);
		assertTrue(ejecutor.getVidaActual() > vidaTrasDanio);

		ejecutor.restaurarParaCombate();
		ejecutor.ejecutarTecnica(meditacion, ejecutor);
		assertEquals(ejecutor.getVidaMaxima(), ejecutor.getVidaActual());
	}
}