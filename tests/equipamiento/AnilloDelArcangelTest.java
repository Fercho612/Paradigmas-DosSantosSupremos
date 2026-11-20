package equipamiento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import combatientes.Combatiente;
import combatientes.CultivadorDeLaEspada;

class AnilloDelArcangelTest {

	private static final int IDENTIFICADOR_AURELIO = 1;
	private static final String NOMBRE_AURELIO = "Aurelio";
	private static final int DANIO_QUE_SUMA = 10;
	private static final int SIN_BONUS = 0;

	private Combatiente aurelio;
	private Equipamiento anillo;
	private int vidaMaximaSinObjetos;
	private int energiaMaximaSinObjetos;

	@BeforeEach
	void prepararEscenario() {
		aurelio = new CultivadorDeLaEspada(IDENTIFICADOR_AURELIO, NOMBRE_AURELIO);
		anillo = new AnilloDelArcangel();
		vidaMaximaSinObjetos = aurelio.getVidaMaxima();
		energiaMaximaSinObjetos = aurelio.getEnergiaMaxima();
	}

	@Test
	void sumaDanio() {
		// Act
		aurelio.equipar(anillo);

		// Assert
		assertEquals(DANIO_QUE_SUMA, aurelio.getBonusDmgTotal());
	}

	@Test
	void noModificaOtrasCapacidades() {
		// Act
		aurelio.equipar(anillo);

		// Assert
		assertEquals(SIN_BONUS, aurelio.getReduccionDmgTotal());
		assertEquals(vidaMaximaSinObjetos, aurelio.getVidaMaxima());
		assertEquals(energiaMaximaSinObjetos, aurelio.getEnergiaMaxima());
	}

	@Test
	void noConviveConAnilloDeDoran() {
		// Arrange
		Equipamiento doran = new AnilloDeDoran();
		aurelio.equipar(doran);

		// Act
		Executable equiparAnillo = () -> aurelio.equipar(anillo);

		// Assert
		assertThrows(EquipamientoInvalidoException.class, equiparAnillo);
		assertEquals(List.of(doran), aurelio.getEquipamiento());
	}
}
