package equipamiento;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import combatientes.Combatiente;
import combatientes.CultivadorDeLaEspada;

class RostroEspiritualTest {

	private static final int IDENTIFICADOR_AURELIO = 1;
	private static final String NOMBRE_AURELIO = "Aurelio";
	private static final int VIDA_QUE_SUMA = 150;
	private static final int SIN_BONUS = 0;

	private Combatiente aurelio;
	private Equipamiento rostro;
	private int vidaMaximaSinObjetos;
	private int energiaMaximaSinObjetos;

	@BeforeEach
	void prepararEscenario() {
		aurelio = new CultivadorDeLaEspada(IDENTIFICADOR_AURELIO, NOMBRE_AURELIO);
		rostro = new RostroEspiritual();
		vidaMaximaSinObjetos = aurelio.getVidaMaxima();
		energiaMaximaSinObjetos = aurelio.getEnergiaMaxima();
	}

	@Test
	void sumaVidaMaxima() {
		// Act
		aurelio.equipar(rostro);

		// Assert
		assertEquals(vidaMaximaSinObjetos + VIDA_QUE_SUMA, aurelio.getVidaMaxima());
	}

	@Test
	void noModificaOtrasCapacidades() {
		// Act
		aurelio.equipar(rostro);

		// Assert
		assertEquals(SIN_BONUS, aurelio.getBonusDmgTotal());
		assertEquals(SIN_BONUS, aurelio.getReduccionDmgTotal());
		assertEquals(energiaMaximaSinObjetos, aurelio.getEnergiaMaxima());
	}

	@Test
	void noConviveConVeloDeBanshee() {
		// Arrange
		Equipamiento velo = new VeloDeBanshee();
		aurelio.equipar(velo);

		// Act
		Executable equiparRostro = () -> aurelio.equipar(rostro);

		// Assert
		assertThrows(EquipamientoInvalidoException.class, equiparRostro);
		assertEquals(List.of(velo), aurelio.getEquipamiento());
	}
}
