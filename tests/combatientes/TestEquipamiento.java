package combatientes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import equipamiento.CorazonDeAcero;
import equipamiento.DanzaDeLaMuerte;
import equipamiento.EquipamientoInvalidoException;
import equipamiento.VeloDeBanshee;

class TestEquipamiento {

	@Test
	void testEquiparMismaCategoriaDeEquipamiento() {
		Combatiente dante = new CultivadorDeLaEspada(1, "Dante");
		dante.equipar(new DanzaDeLaMuerte());
		assertThrows(EquipamientoInvalidoException.class,
				() -> dante.equipar(new DanzaDeLaMuerte()));
		assertEquals(1, dante.getEquipamiento().size());
	}
	
	@Test
	void testEquiparUnCuartoObjeto() {
		Combatiente dante = new CultivadorDeLaEspada(1, "Dante");
		dante.equipar(new DanzaDeLaMuerte());
		dante.equipar(new CorazonDeAcero());
		dante.equipar(new VeloDeBanshee());
		assertThrows(EquipamientoInvalidoException.class, 
				() -> dante.equipar(new DanzaDeLaMuerte()));
		assertEquals(3, dante.getEquipamiento().size());
	}
	
	@Test
	void testCambioDeStatsPorEquipamiento() {
		Combatiente dante = new CultivadorDeLaEspada(1, "Dante");
		int vidaAct = dante.getVidaMaxima();		
		dante.equipar(new CorazonDeAcero());
		int vidaConObj = dante.getVidaMaxima();
		assertNotEquals(vidaAct, vidaConObj);
	}
}
