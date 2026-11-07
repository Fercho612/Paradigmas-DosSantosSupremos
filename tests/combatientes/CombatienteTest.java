package combatientes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CombatienteTest {

	@Test
	void testCombatientesMismoID() {
		Combatiente dante = new CultivadorDeLaEspada(1, "Dante");
		Combatiente buda = new MonjeDelCuerpoCelestial(1, "Buda");
		assertEquals(dante, buda);
		assertEquals(dante.hashCode(), buda.hashCode());
	}
	
	@Test
	void testCombatientesDistintoID() {
		Combatiente dante1 = new CultivadorDeLaEspada(1, "Dante");
		Combatiente dante2 = new CultivadorDeLaEspada(2, "Dante");
		assertNotEquals(dante1, dante2);
	}
}
