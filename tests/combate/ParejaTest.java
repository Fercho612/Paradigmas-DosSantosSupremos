package combate;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import combatientes.*;
import tecnicas.*;

class ParejaTest {

	private Tecnica corte = new Tecnica("Corte de los Mil Amaneceres", 30, TipoTecnica.OFENSIVA,
			(ejecutor, objetivo) -> objetivo.recibirDanio(ejecutor.modificarDanioProvocado(100)));

	// ===== Ordenamiento por ID =====

	@Test
	void integrante1EsElDeMenorId() {
		Combatiente a = new CultivadorDeLaEspada(5, "A");
		Combatiente b = new MonjeDelCuerpoCelestial(3, "B");
		Pareja p = new Pareja(10, a, b);

		assertEquals(3, p.getIntegrante1().getIdentificador());
	}

	@Test
	void integrante2EsElDeMayorId() {
		Combatiente a = new CultivadorDeLaEspada(5, "A");
		Combatiente b = new MonjeDelCuerpoCelestial(3, "B");
		Pareja p = new Pareja(10, a, b);

		assertEquals(5, p.getIntegrante2().getIdentificador());
	}

	// ===== Eliminación =====

	@Test
	void noEstaEliminadaConAmbosEnPie() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);

		assertFalse(p.estaEliminada());
	}

	@Test
	void noEstaEliminadaSiCaeUnoSolo() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);
		c1.recibirDanio(99999);

		assertFalse(p.estaEliminada());
	}

	@Test
	void estaEliminadaCuandoCaenAmbos() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);
		c1.recibirDanio(99999);
		c2.recibirDanio(99999);

		assertTrue(p.estaEliminada());
	}

	// ===== Vida total =====

	@Test
	void vidaTotalSumaAmbosIntegrantes() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);

		assertEquals(c1.getVidaActual() + c2.getVidaActual(), p.getVidaTotal());
	}

	@Test
	void vidaTotalCaidoAportaCero() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);
		c1.recibirDanio(99999);

		assertEquals(c2.getVidaActual(), p.getVidaTotal());
	}

	// ===== Integrantes en pie =====

	@Test
	void integrantesEnPieDevuelveDosAlInicio() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);

		assertEquals(2, p.getIntegrantesEnPie().size());
	}

	@Test
	void integrantesEnPieExcluyeCaido() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);
		c1.recibirDanio(99999);

		assertEquals(1, p.getIntegrantesEnPie().size());
	}

	@Test
	void integrantesEnPieDevuelveAlSobreviviente() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);
		c1.recibirDanio(99999);

		assertEquals(c2, p.getIntegrantesEnPie().get(0));
	}

	// ===== Regla del compañero =====

	@Test
	void companeroNoPuedeUsarTecnicaRegistradaPorElOtro() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		c1.setTecnicas(List.of(corte));
		c2.setTecnicas(List.of(corte));
		Pareja p = new Pareja(1, c1, c2);

		p.registrarUsoTecnica(c1, corte);

		assertFalse(p.puedeUsarTecnica(c2, corte));
	}

	@Test
	void quienRegistroLaTecnicaSiPuedeSeguirUsandola() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		c1.setTecnicas(List.of(corte));
		c2.setTecnicas(List.of(corte));
		Pareja p = new Pareja(1, c1, c2);

		p.registrarUsoTecnica(c1, corte);

		assertTrue(p.puedeUsarTecnica(c1, corte));
	}

	@Test
	void companeroSeLiberaSiElPrimeroCae() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		c1.setTecnicas(List.of(corte));
		c2.setTecnicas(List.of(corte));
		Pareja p = new Pareja(1, c1, c2);

		p.registrarUsoTecnica(c1, corte);
		c1.recibirDanio(99999);

		assertTrue(p.puedeUsarTecnica(c2, corte));
	}

	@Test
	void companeroSeLiberaSiElPrimeroAgotaUsos() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		c1.setTecnicas(List.of(corte));
		c2.setTecnicas(List.of(corte));
		Pareja p = new Pareja(1, c1, c2);

		for (int i = 0; i < 5; i++) {
			p.registrarUsoTecnica(c1, corte);
		}

		assertTrue(p.puedeUsarTecnica(c2, corte));
	}

	@Test
	void reglasCompaneroSeLimpianAlRestaurar() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		c1.setTecnicas(List.of(corte));
		c2.setTecnicas(List.of(corte));
		Pareja p = new Pareja(1, c1, c2);

		p.registrarUsoTecnica(c1, corte);
		p.limpiarReglasCompanero();

		assertTrue(p.puedeUsarTecnica(c2, corte));
	}

	// ===== Contiene y compañero =====

	@Test
	void contieneIntegrante() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);

		assertTrue(p.contiene(c1));
	}

	@Test
	void noContieneAjeno() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);
		Combatiente otro = new CultivadorDeLaEspada(99, "Otro");

		assertFalse(p.contiene(otro));
	}

	@Test
	void companeroDelPrimeroEsElSegundo() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);

		assertEquals(c2, p.getCompanero(c1));
	}

	@Test
	void companeroDelSegundoEsElPrimero() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);

		assertEquals(c1, p.getCompanero(c2));
	}

	@Test
	void getCompaneroLanzaExcepcionSiNoPertenece() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p = new Pareja(1, c1, c2);
		Combatiente otro = new CultivadorDeLaEspada(99, "Otro");

		assertThrows(IllegalArgumentException.class, () -> p.getCompanero(otro));
	}

	// ===== Equals =====

	@Test
	void parejasConMismoIdSonIguales() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p1 = new Pareja(1, c1, c2);
		Pareja p2 = new Pareja(1, c1, c2);

		assertEquals(p1, p2);
	}

	@Test
	void parejasConDistintoIdSonDistintas() {
		Combatiente c1 = new CultivadorDeLaEspada(1, "Aurelio");
		Combatiente c2 = new HechiceroDelVacio(2, "Nébula");
		Pareja p1 = new Pareja(1, c1, c2);
		Pareja p2 = new Pareja(2, c1, c2);

		assertNotEquals(p1, p2);
	}
}
