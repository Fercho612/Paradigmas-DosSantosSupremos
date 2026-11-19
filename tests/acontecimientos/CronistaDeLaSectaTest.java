package acontecimientos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class CronistaDeLaSectaTest {

	// ===== Historial =====

	@Test
	void historialRegistraLosAcontecimientos() {
		CronistaDeLaSecta cronista = new CronistaDeLaSecta();
		cronista.notificar(new Eliminacion(1, "Aurelio"));
		cronista.notificar(new ResultadoCombate(1, 1, 500, 2, 0));

		assertEquals(2, cronista.getHistorial().size());
	}

	@Test
	void historialConservaElOrden() {
		CronistaDeLaSecta cronista = new CronistaDeLaSecta();
		Acontecimiento eliminacion = new Eliminacion(1, "Aurelio");
		cronista.notificar(eliminacion);
		cronista.notificar(new ResultadoCombate(1, 1, 500, 2, 0));

		assertEquals(eliminacion, cronista.getHistorial().get(0));
	}

	// ===== Tabla de posiciones =====

	@Test
	void tablaOrdenaPorUltimaRonda() {
		CronistaDeLaSecta cronista = new CronistaDeLaSecta();
		cronista.notificar(new ResultadoCombate(1, 1, 100, 2, 0));
		cronista.notificar(new ResultadoCombate(1, 3, 900, 4, 0));
		cronista.notificar(new ResultadoCombate(2, 1, 50, 3, 0));

		List<Posicion> tabla = cronista.getTablaDePosiciones();

		assertEquals(List.of(1, 3, 2, 4), idsDe(tabla));
	}

	@Test
	void tablaDesempataPorVidaTotal() {
		CronistaDeLaSecta cronista = new CronistaDeLaSecta();
		cronista.notificar(new ResultadoCombate(1, 1, 300, 2, 100));
		cronista.notificar(new ResultadoCombate(1, 3, 800, 4, 200));

		List<Posicion> tabla = cronista.getTablaDePosiciones();

		assertEquals(List.of(3, 1, 4, 2), idsDe(tabla));
	}

	@Test
	void tablaDesempataPorIdentificador() {
		CronistaDeLaSecta cronista = new CronistaDeLaSecta();
		cronista.notificar(new ResultadoCombate(1, 4, 500, 3, 0));
		cronista.notificar(new ResultadoCombate(1, 2, 500, 1, 0));

		List<Posicion> tabla = cronista.getTablaDePosiciones();

		assertEquals(List.of(2, 4, 1, 3), idsDe(tabla));
	}

	@Test
	void tablaGuardaLaVidaDelUltimoCombate() {
		CronistaDeLaSecta cronista = new CronistaDeLaSecta();
		cronista.notificar(new ResultadoCombate(1, 1, 900, 2, 0));
		cronista.notificar(new ResultadoCombate(2, 3, 400, 1, 100));

		assertEquals(100, buscar(cronista.getTablaDePosiciones(), 1).getVidaTotalFinal());
	}

	// ===== Helpers privados =====

	private List<Integer> idsDe(List<Posicion> tabla) {
		return tabla.stream().map(Posicion::getIdPareja).toList();
	}

	private Posicion buscar(List<Posicion> tabla, int idPareja) {
		for (Posicion p : tabla) {
			if (p.getIdPareja() == idPareja) {
				return p;
			}
		}
		return null;
	}
}
