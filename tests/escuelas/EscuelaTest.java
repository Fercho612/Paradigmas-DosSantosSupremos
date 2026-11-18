package escuelas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import equipamiento.AnilloDeDoran;
import equipamiento.CorazonDeAcero;
import equipamiento.DanzaDeLaMuerte;
import equipamiento.EquipamientoInvalidoException;
import equipamiento.VeloDeBanshee;
import tecnicas.CatalogoTecnicas;

class EscuelaTest {

	@Test
	void unaEscuelaValidaConservaNombreTecnicasYEquipamiento() {
		Escuela escuela = new Escuela("Escuela X",
				List.of(CatalogoTecnicas.CORTE_DE_LOS_MIL_AMANECERES,
						CatalogoTecnicas.MURO_DEL_CUERPO_DORADO),
				List.of(new DanzaDeLaMuerte(), new CorazonDeAcero()));

		assertEquals("Escuela X", escuela.getNombre());
		assertEquals(2, escuela.getTecnicas().size());
		assertEquals(CatalogoTecnicas.CORTE_DE_LOS_MIL_AMANECERES, escuela.getTecnicas().get(0));
		assertEquals(2, escuela.getEquipamiento().size());
	}

	@Test
	void rechazaMasDeTresObjetosAlCrearse() {
		List<equipamiento.Equipamiento> cuatro = List.of(
				new DanzaDeLaMuerte(), new CorazonDeAcero(),
				new AnilloDeDoran(), new VeloDeBanshee());

		assertThrows(EquipamientoInvalidoException.class,
				() -> new Escuela("Escuela X", List.of(), cuatro));
	}

	@Test
	void rechazaDosObjetosDeLaMismaCategoriaAlCrearse() {
		List<equipamiento.Equipamiento> repetidos = List.of(
				new DanzaDeLaMuerte(), new DanzaDeLaMuerte());

		assertThrows(EquipamientoInvalidoException.class,
				() -> new Escuela("Escuela X", List.of(), repetidos));
	}

	@Test
	void rechazaUnNombreVacio() {
		assertThrows(IllegalArgumentException.class,
				() -> new Escuela("  ", List.of(), List.of()));
	}

	@Test
	void lasListasDeLaEscuelaNoSePuedenModificarDesdeAfuera() {
		Escuela escuela = CatalogoEscuelas.ESPADA_CELESTIAL;

		assertThrows(UnsupportedOperationException.class,
				() -> escuela.getTecnicas().add(CatalogoTecnicas.MURO_DEL_CUERPO_DORADO));
		assertThrows(UnsupportedOperationException.class,
				() -> escuela.getEquipamiento().clear());
	}

	@Test
	void lasTresEscuelasBaseSeCreanCorrectamente() {
		assertEquals("Escuela de la Espada Celestial", CatalogoEscuelas.ESPADA_CELESTIAL.getNombre());
		assertEquals("Palacio del Vacío", CatalogoEscuelas.PALACIO_DEL_VACIO.getNombre());
		assertEquals("Monasterio del Cuerpo Dorado",
				CatalogoEscuelas.MONASTERIO_DEL_CUERPO_DORADO.getNombre());

		// El orden de las técnicas es el orden de preferencia de la escuela.
		assertEquals(CatalogoTecnicas.DOMINIO_DE_LA_NADA,
				CatalogoEscuelas.PALACIO_DEL_VACIO.getTecnicas().get(0));
	}
}