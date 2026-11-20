package torneo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import acontecimientos.CronistaDeLaSecta;
import combate.Pareja;
import combatientes.*;
import escuelas.CatalogoEscuelas;

class TorneoTest {

	private static final int PRIMER_IDENTIFICADOR = 1;
	private static final int SEGUNDO_IDENTIFICADOR = 2;
	private static final int UNA_PAREJA = 1;

	// ===== Inscripciones duplicadas se rechazan =====

	@Test
	void parejaDuplicadaSeRechaza() {
		CreadorCombatientes creador = new CreadorCombatientes();
		Torneo torneo = new Torneo();
		Pareja pareja = crearPareja(creador, 1);
		torneo.inscribir(pareja);

		assertThrows(InscripcionInvalidaException.class, () -> torneo.inscribir(pareja));
	}

	@Test
	void combatienteYaInscriptoSeRechaza() {
		CreadorCombatientes creador = new CreadorCombatientes();
		Torneo torneo = new Torneo();
		Pareja primera = crearPareja(creador, 1);
		torneo.inscribir(primera);
		Combatiente otro = creador.crear(TipoCombatiente.MONJE_DEL_CUERPO_CELESTIAL,
				CatalogoEscuelas.MONASTERIO_DEL_CUERPO_DORADO, "Otro");
		Pareja conRepetido = new Pareja(2, primera.getIntegrante1(), otro);

		assertThrows(InscripcionInvalidaException.class, () -> torneo.inscribir(conRepetido));
	}

	// ===== El identificador de la pareja se asigna al inscribirla =====

	@Test
	void inscribirDosCombatientesAsignaIdentificadoresCorrelativos() {
		// Arrange
		CreadorCombatientes creador = new CreadorCombatientes();
		Torneo torneo = new Torneo();

		// Act
		Pareja primera = torneo.inscribir(nuevoCombatiente(creador, "Uno"), nuevoCombatiente(creador, "Dos"));
		Pareja segunda = torneo.inscribir(nuevoCombatiente(creador, "Tres"), nuevoCombatiente(creador, "Cuatro"));

		// Assert
		assertEquals(PRIMER_IDENTIFICADOR, primera.getIdentificador());
		assertEquals(SEGUNDO_IDENTIFICADOR, segunda.getIdentificador());
	}

	@Test
	void inscribirDosCombatientesDejaALaParejaInscripta() {
		// Arrange
		CreadorCombatientes creador = new CreadorCombatientes();
		Torneo torneo = new Torneo();
		Combatiente uno = nuevoCombatiente(creador, "Uno");
		Combatiente dos = nuevoCombatiente(creador, "Dos");

		// Act
		Pareja pareja = torneo.inscribir(uno, dos);

		// Assert
		assertEquals(List.of(pareja), torneo.getParejas());
		assertEquals(List.of(uno, dos), pareja.getIntegrantes());
	}

	@Test
	void inscribirDosCombatientesConUnoYaInscriptoSeRechaza() {
		// Arrange
		CreadorCombatientes creador = new CreadorCombatientes();
		Torneo torneo = new Torneo();
		Combatiente repetido = nuevoCombatiente(creador, "Repetido");
		torneo.inscribir(repetido, nuevoCombatiente(creador, "Otro"));
		Combatiente nuevo = nuevoCombatiente(creador, "Nuevo");

		// Act + Assert
		assertThrows(InscripcionInvalidaException.class, () -> torneo.inscribir(repetido, nuevo));
		assertEquals(UNA_PAREJA, torneo.getParejas().size());
	}

	// ===== Cantidad de parejas inválida se rechaza =====

	@Test
	void torneoConTresParejasSeRechaza() {
		Torneo torneo = crearTorneo(3, new CronistaDeLaSecta());

		assertThrows(InscripcionInvalidaException.class, () -> torneo.iniciar());
	}

	@Test
	void torneoConCincoParejasSeRechaza() {
		Torneo torneo = crearTorneo(5, new CronistaDeLaSecta());

		assertThrows(InscripcionInvalidaException.class, () -> torneo.iniciar());
	}

	@Test
	void torneoConCuatroParejasTieneCampeona() {
		Torneo torneo = crearTorneo(4, new CronistaDeLaSecta());
		torneo.iniciar();

		assertNotNull(torneo.getCampeona());
	}

	@Test
	void torneoConOchoParejasTieneCampeona() {
		Torneo torneo = crearTorneo(8, new CronistaDeLaSecta());
		torneo.iniciar();

		assertNotNull(torneo.getCampeona());
	}

	// ===== Rondas: con 8 parejas se juegan 3 rondas =====

	@Test
	void campeonaDeOchoParejasLlegaALaTerceraRonda() {
		CronistaDeLaSecta cronista = new CronistaDeLaSecta();
		Torneo torneo = crearTorneo(8, cronista);
		torneo.iniciar();

		assertEquals(3, cronista.getTablaDePosiciones().get(0).getUltimaRonda());
	}

	// ===== Dos torneos iguales dan el mismo campeón e historial =====

	@Test
	void dosTorneosIgualesMismoCampeon() {
		Torneo torneo1 = crearTorneo(8, new CronistaDeLaSecta());
		Torneo torneo2 = crearTorneo(8, new CronistaDeLaSecta());

		assertEquals(torneo1.iniciar().getIdentificador(), torneo2.iniciar().getIdentificador());
	}

	@Test
	void dosTorneosIgualesMismoHistorial() {
		CronistaDeLaSecta cronista1 = new CronistaDeLaSecta();
		CronistaDeLaSecta cronista2 = new CronistaDeLaSecta();
		crearTorneo(8, cronista1).iniciar();
		crearTorneo(8, cronista2).iniciar();

		assertEquals(cronista1.getHistorial().toString(), cronista2.getHistorial().toString());
	}

	// ===== Helpers privados =====

	private Torneo crearTorneo(int cantidadParejas, CronistaDeLaSecta cronista) {
		CreadorCombatientes creador = new CreadorCombatientes();
		Torneo torneo = new Torneo();
		torneo.agregarInteresado(cronista);
		for (int i = 1; i <= cantidadParejas; i++) {
			torneo.inscribir(crearPareja(creador, i));
		}
		return torneo;
	}

	private Combatiente nuevoCombatiente(CreadorCombatientes creador, String nombre) {
		return creador.crear(TipoCombatiente.MONJE_DEL_CUERPO_CELESTIAL,
				CatalogoEscuelas.MONASTERIO_DEL_CUERPO_DORADO, nombre);
	}

	private Pareja crearPareja(CreadorCombatientes creador, int idPareja) {
		Combatiente c1 = creador.crear(TipoCombatiente.CULTIVADOR_DE_LA_ESPADA,
				CatalogoEscuelas.ESPADA_CELESTIAL, "Espadachin" + idPareja);
		Combatiente c2;
		if (idPareja % 2 == 0) {
			c2 = creador.crear(TipoCombatiente.HECHICERO_DEL_VACIO,
					CatalogoEscuelas.PALACIO_DEL_VACIO, "Hechicero" + idPareja);
		} else {
			c2 = creador.crear(TipoCombatiente.MONJE_DEL_CUERPO_CELESTIAL,
					CatalogoEscuelas.MONASTERIO_DEL_CUERPO_DORADO, "Monje" + idPareja);
		}
		return new Pareja(idPareja, c1, c2);
	}
}
