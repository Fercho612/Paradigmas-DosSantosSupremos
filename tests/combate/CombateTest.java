package combate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import combatientes.*;
import tecnicas.*;

class CombateTest {

	private Tecnica corte = new Tecnica("Corte de los Mil Amaneceres", 30, TipoTecnica.OFENSIVA,
			(ejecutor, objetivo) -> {
				int danio = ejecutor.modificarDanioProvocado(100 + ejecutor.getBonusDmgTotal());
				objetivo.recibirDanio(danio);
			});

	private Tecnica muro = new Tecnica("Muro del Cuerpo Dorado", 25, TipoTecnica.DEFENSIVA,
			(ejecutor, objetivo) -> ejecutor.agregarReduccionTemporal(30));

	private Tecnica meditacion = new Tecnica("Meditación del Inmortal", 35, TipoTecnica.RECUPERACION,
			(ejecutor, objetivo) -> ejecutor.curar(150));

	private Tecnica dominio = new Tecnica("Dominio de la Nada", 50, TipoTecnica.OFENSIVA,
			(ejecutor, objetivo) -> {
				int danio = ejecutor.modificarDanioProvocado(120 + ejecutor.getBonusDmgTotal());
				objetivo.recibirDanio(danio);
				objetivo.gastarEnergia(20);
			});

	// ===== El combate termina con un ganador =====

	@Test
	void combateDevuelveGanadorNoNulo() {
		Pareja p1 = crearParejaOfensiva(1, 1, 2);
		Pareja p2 = crearParejaDefensiva(2, 3, 4);
		Combate combate = new Combate(p1, p2);

		assertNotNull(combate.ejecutar());
	}

	@Test
	void combateQuedaFinalizado() {
		Pareja p1 = crearParejaOfensiva(1, 1, 2);
		Pareja p2 = crearParejaDefensiva(2, 3, 4);
		Combate combate = new Combate(p1, p2);
		combate.ejecutar();

		assertTrue(combate.isFinalizado());
	}

	// ===== Vida y energía se recuperan al inicio de cada combate =====

	@Test
	void vidaSeRecuperaAlIniciarNuevoCombate() {
		Pareja p1 = crearParejaOfensiva(1, 1, 2);
		Pareja p2 = crearParejaDefensiva(2, 3, 4);
		Combate combate1 = new Combate(p1, p2);
		combate1.ejecutar();

		Pareja p3 = crearParejaOfensiva(3, 5, 6);
		new Combate(p1, p3);

		assertEquals(p1.getIntegrante1().getVidaMaxima(), p1.getIntegrante1().getVidaActual());
	}

	@Test
	void energiaSeRecuperaAlIniciarNuevoCombate() {
		Pareja p1 = crearParejaOfensiva(1, 1, 2);
		Pareja p2 = crearParejaDefensiva(2, 3, 4);
		Combate combate1 = new Combate(p1, p2);
		combate1.ejecutar();

		Pareja p3 = crearParejaOfensiva(3, 5, 6);
		new Combate(p1, p3);

		assertEquals(p1.getIntegrante1().getEnergiaMaxima(), p1.getIntegrante1().getEnergiaActual());
	}

	@Test
	void caidoReviveAlIniciarNuevoCombate() {
		Pareja p1 = crearParejaOfensiva(1, 1, 2);
		Pareja p2 = crearParejaDefensiva(2, 3, 4);
		Combate combate1 = new Combate(p1, p2);
		combate1.ejecutar();

		Pareja p3 = crearParejaOfensiva(3, 5, 6);
		new Combate(p1, p3);

		assertFalse(p1.getIntegrante1().estaFueraDeCombate());
	}

	// ===== Los usos de técnicas se conservan entre combates =====

	@Test
	void usosDeTenicasSeConservanEntreCombates() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "C1");
		Combatiente c2 = new CultivadorDeLaEspada(2, "C2");
		c1.setTecnicas(List.of(corte));
		c2.setTecnicas(List.of(corte));
		Pareja p1 = new Pareja(1, c1, c2);

		Combatiente c3 = new MonjeDelCuerpoCelestial(3, "C3");
		Combatiente c4 = new MonjeDelCuerpoCelestial(4, "C4");
		c3.setTecnicas(List.of(muro, meditacion, corte));
		c4.setTecnicas(List.of(muro, meditacion, corte));
		Pareja p2 = new Pareja(2, c3, c4);

		Combate combate1 = new Combate(p1, p2);
		combate1.ejecutar();
		int usosTrasComabte1 = c1.getUsosRestantes(corte);

		Combatiente c5 = new MonjeDelCuerpoCelestial(5, "C5");
		Combatiente c6 = new MonjeDelCuerpoCelestial(6, "C6");
		c5.setTecnicas(List.of(muro, meditacion, corte));
		c6.setTecnicas(List.of(muro, meditacion, corte));
		Pareja p3 = new Pareja(3, c5, c6);
		new Combate(p1, p3);

		assertEquals(usosTrasComabte1, c1.getUsosRestantes(corte));
	}

	// ===== Un caído no actúa ni es objetivo =====

	@Test
	void caidoTieneVidaCero() {
		Combatiente c = new CultivadorDeLaEspada(1, "Test");
		c.recibirDanio(99999);

		assertEquals(0, c.getVidaActual());
	}

	@Test
	void caidoEstaFueraDeCombate() {
		Combatiente c = new CultivadorDeLaEspada(1, "Test");
		c.recibirDanio(99999);

		assertTrue(c.estaFueraDeCombate());
	}

	@Test
	void caidoNoApareceEnIntegrantesEnPie() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "C1");
		Combatiente c2 = new CultivadorDeLaEspada(2, "C2");
		Pareja p = new Pareja(1, c1, c2);
		c1.recibirDanio(99999);

		assertFalse(p.getIntegrantesEnPie().contains(c1));
	}

	// ===== Ataque básico reduce vida =====

	@Test
	void ataqueBasicoReduceVidaDelObjetivo() {
		Combatiente atacante = new CultivadorDeLaEspada(1, "Atacante");
		Combatiente objetivo = new MonjeDelCuerpoCelestial(2, "Objetivo");
		int vidaAntes = objetivo.getVidaActual();

		atacante.ejecutarAtaqueBasico(objetivo);

		assertTrue(objetivo.getVidaActual() < vidaAntes);
	}

	// ===== Desempate al límite de asaltos =====

	@Test
	void desempateAlLimiteGanadorTieneMasOIgualVida() {
		Combatiente c1 = new MonjeDelCuerpoCelestial(1, "M1");
		Combatiente c2 = new MonjeDelCuerpoCelestial(2, "M2");
		c1.setTecnicas(List.of(muro, meditacion));
		c2.setTecnicas(List.of(muro, meditacion));
		Pareja p1 = new Pareja(1, c1, c2);

		Combatiente c3 = new MonjeDelCuerpoCelestial(3, "M3");
		Combatiente c4 = new MonjeDelCuerpoCelestial(4, "M4");
		c3.setTecnicas(List.of(muro, meditacion));
		c4.setTecnicas(List.of(muro, meditacion));
		Pareja p2 = new Pareja(2, c3, c4);

		Combate combate = new Combate(p1, p2);
		Pareja ganadora = combate.ejecutar();

		assertTrue(ganadora.getVidaTotal() >= combate.getPerdedora().getVidaTotal());
	}

	// ===== Combate determinista =====

	@Test
	void dosCombatesIgualesMismoGanador() {
		Pareja p1a = crearParejaOfensiva(1, 1, 2);
		Pareja p2a = crearParejaDefensiva(2, 3, 4);
		Combate combate1 = new Combate(p1a, p2a);
		int idGanador1 = combate1.ejecutar().getIdentificador();

		Pareja p1b = crearParejaOfensiva(1, 1, 2);
		Pareja p2b = crearParejaDefensiva(2, 3, 4);
		Combate combate2 = new Combate(p1b, p2b);
		int idGanador2 = combate2.ejecutar().getIdentificador();

		assertEquals(idGanador1, idGanador2);
	}

	@Test
	void dosCombatesIgualesMismoAsaltoFinal() {
		Pareja p1a = crearParejaOfensiva(1, 1, 2);
		Pareja p2a = crearParejaDefensiva(2, 3, 4);
		Combate combate1 = new Combate(p1a, p2a);
		combate1.ejecutar();

		Pareja p1b = crearParejaOfensiva(1, 1, 2);
		Pareja p2b = crearParejaDefensiva(2, 3, 4);
		Combate combate2 = new Combate(p1b, p2b);
		combate2.ejecutar();

		assertEquals(combate1.getAsaltoActual(), combate2.getAsaltoActual());
	}

	// ===== Misma técnica daña distinto según tipo =====

	@Test
	void espadaDaniaMasQueHechiceroConMismaTecnica() {
		Combatiente espada = new CultivadorDeLaEspada(1, "Espada");
		Combatiente hechicero = new HechiceroDelVacio(2, "Hechicero");
		Combatiente obj1 = new MonjeDelCuerpoCelestial(10, "Obj1");
		Combatiente obj2 = new MonjeDelCuerpoCelestial(11, "Obj2");
		int vidaAntes1 = obj1.getVidaActual();
		int vidaAntes2 = obj2.getVidaActual();

		corte.ejecutar(espada, obj1);
		corte.ejecutar(hechicero, obj2);

		assertTrue((vidaAntes1 - obj1.getVidaActual()) > (vidaAntes2 - obj2.getVidaActual()));
	}

	// ===== No se ejecuta técnica sin energía =====

	@Test
	void noPuedeUsarTecnicaSinEnergia() {
		Combatiente c = new CultivadorDeLaEspada(1, "Test");
		Tecnica cara = new Tecnica("Cara", 9999, TipoTecnica.OFENSIVA, (ej, obj) -> obj.recibirDanio(10));
		c.setTecnicas(List.of(cara));

		assertFalse(c.puedeUsarTecnica(cara));
	}

	// ===== No se ejecuta técnica sin usos =====

	@Test
	void noPuedeUsarTecnicaSinUsos() {
		Combatiente c = new CultivadorDeLaEspada(1, "Test");
		c.setTecnicas(List.of(corte));
		for (int i = 0; i < 5; i++) {
			c.registrarUsoTecnica(corte);
		}

		assertFalse(c.puedeUsarTecnica(corte));
	}

	// ===== Pareja de menor ID actúa primero =====

	@Test
	void pareja1EnCombateEsLaDeMenorId() {
		Pareja p1 = crearParejaOfensiva(5, 1, 2);
		Pareja p2 = crearParejaDefensiva(3, 3, 4);
		Combate combate = new Combate(p1, p2);

		assertEquals(3, combate.getPareja1().getIdentificador());
	}

	// ===== Recuperación de energía por asalto =====

	@Test
	void recuperarEnergiaFunciona() {
		Combatiente c = new CultivadorDeLaEspada(1, "C1");
		c.gastarEnergia(c.getEnergiaActual());

		c.recuperarEnergia();

		assertEquals(Combatiente.getEnergiaPorAsalto(), c.getEnergiaActual());
	}

	// ===== Monje recibe menos daño =====

	@Test
	void monjeRecibeMenosDanioQueEspada() {
		Combatiente monje = new MonjeDelCuerpoCelestial(1, "Monje");
		Combatiente espada = new CultivadorDeLaEspada(2, "Espada");
		int vidaMonje = monje.getVidaActual();
		int vidaEspada = espada.getVidaActual();

		monje.recibirDanio(100);
		espada.recibirDanio(100);

		assertTrue((vidaMonje - monje.getVidaActual()) < (vidaEspada - espada.getVidaActual()));
	}

	// ===== Hechicero gasta menos energía =====

	@Test
	void hechiceroGastaMenosEnergia() {
		Combatiente hechicero = new HechiceroDelVacio(1, "Hechicero");
		Combatiente espada = new CultivadorDeLaEspada(2, "Espada");

		assertTrue(hechicero.modificarCostoEnergia(30) < espada.modificarCostoEnergia(30));
	}

	// ===== Helpers privados =====

	private Pareja crearParejaOfensiva(int idPareja, int id1, int id2) {
		Combatiente c1 = new CultivadorDeLaEspada(id1, "Espadachín" + id1);
		Combatiente c2 = new CultivadorDeLaEspada(id2, "Espadachín" + id2);
		c1.setTecnicas(List.of(corte, dominio));
		c2.setTecnicas(List.of(corte, dominio));
		return new Pareja(idPareja, c1, c2);
	}

	private Pareja crearParejaDefensiva(int idPareja, int id1, int id2) {
		Combatiente c1 = new MonjeDelCuerpoCelestial(id1, "Monje" + id1);
		Combatiente c2 = new MonjeDelCuerpoCelestial(id2, "Monje" + id2);
		c1.setTecnicas(List.of(muro, meditacion, corte));
		c2.setTecnicas(List.of(muro, meditacion, corte));
		return new Pareja(idPareja, c1, c2);
	}
}
