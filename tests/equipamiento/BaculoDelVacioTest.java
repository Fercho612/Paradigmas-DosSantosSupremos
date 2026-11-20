package equipamiento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import combatientes.Combatiente;
import combatientes.CultivadorDeLaEspada;

class BaculoDelVacioTest {

	private static final int IDENTIFICADOR_AURELIO = 1;
	private static final String NOMBRE_AURELIO = "Aurelio";
	private static final int ENERGIA_QUE_SUMA = 100;
	private static final int SIN_BONUS = 0;

	private Combatiente aurelio;
	private Equipamiento baculo;
	private int vidaMaximaSinObjetos;
	private int energiaMaximaSinObjetos;

	@BeforeEach
	void prepararEscenario() {
		aurelio = new CultivadorDeLaEspada(IDENTIFICADOR_AURELIO, NOMBRE_AURELIO);
		baculo = new BaculoDelVacio();
		vidaMaximaSinObjetos = aurelio.getVidaMaxima();
		energiaMaximaSinObjetos = aurelio.getEnergiaMaxima();
	}

	@Test
	void sumaEnergiaMaxima() {
		// Act
		aurelio.equipar(baculo);

		// Assert
		assertEquals(energiaMaximaSinObjetos + ENERGIA_QUE_SUMA, aurelio.getEnergiaMaxima());
	}

	@Test
	void noModificaOtrasCapacidades() {
		// Act
		aurelio.equipar(baculo);

		// Assert
		assertEquals(SIN_BONUS, aurelio.getBonusDmgTotal());
		assertEquals(SIN_BONUS, aurelio.getReduccionDmgTotal());
		assertEquals(vidaMaximaSinObjetos, aurelio.getVidaMaxima());
	}

	@Test
	void noConviveConDanzaDeLaMuerte() {
		// Arrange
		Equipamiento danza = new DanzaDeLaMuerte();
		aurelio.equipar(danza);

		// Act
		Executable equiparBaculo = () -> aurelio.equipar(baculo);

		// Assert
		assertThrows(EquipamientoInvalidoException.class, equiparBaculo);
		assertEquals(List.of(danza), aurelio.getEquipamiento());
	}
}
