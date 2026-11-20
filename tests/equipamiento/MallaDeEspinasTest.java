package equipamiento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import combatientes.Combatiente;
import combatientes.CultivadorDeLaEspada;

class MallaDeEspinasTest {

	private static final int IDENTIFICADOR_AURELIO = 1;
	private static final String NOMBRE_AURELIO = "Aurelio";
	private static final int REDUCCION_QUE_APORTA = 10;
	private static final int SIN_BONUS = 0;

	private Combatiente aurelio;
	private Equipamiento malla;
	private int vidaMaximaSinObjetos;
	private int energiaMaximaSinObjetos;

	@BeforeEach
	void prepararEscenario() {
		aurelio = new CultivadorDeLaEspada(IDENTIFICADOR_AURELIO, NOMBRE_AURELIO);
		malla = new MallaDeEspinas();
		vidaMaximaSinObjetos = aurelio.getVidaMaxima();
		energiaMaximaSinObjetos = aurelio.getEnergiaMaxima();
	}

	@Test
	void reduceElDanioRecibido() {
		// Act
		aurelio.equipar(malla);

		// Assert
		assertEquals(REDUCCION_QUE_APORTA, aurelio.getReduccionDmgTotal());
	}

	@Test
	void noModificaOtrasCapacidades() {
		// Act
		aurelio.equipar(malla);

		// Assert
		assertEquals(SIN_BONUS, aurelio.getBonusDmgTotal());
		assertEquals(vidaMaximaSinObjetos, aurelio.getVidaMaxima());
		assertEquals(energiaMaximaSinObjetos, aurelio.getEnergiaMaxima());
	}

	@Test
	void noConviveConCorazonDeAcero() {
		// Arrange
		Equipamiento corazon = new CorazonDeAcero();
		aurelio.equipar(corazon);

		// Act
		Executable equiparMalla = () -> aurelio.equipar(malla);

		// Assert
		assertThrows(EquipamientoInvalidoException.class, equiparMalla);
		assertEquals(List.of(corazon), aurelio.getEquipamiento());
	}
}
