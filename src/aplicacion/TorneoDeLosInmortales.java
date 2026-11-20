package aplicacion;

import java.util.ArrayList;
import java.util.List;

import acontecimientos.CronistaDeLaSecta;
import acontecimientos.HeraldoImperial;
import combate.Pareja;
import combatientes.Combatiente;
import combatientes.CreadorCombatientes;
import combatientes.TipoCombatiente;
import efectosTemporales.EfectoTemporal;
import efectosTemporales.EstadoRegeneracion;
import efectosTemporales.EstadoVeneno;
import equipamiento.AnilloDelArcangel;
import equipamiento.BaculoDelVacio;
import equipamiento.Categoria;
import equipamiento.Equipamiento;
import equipamiento.EquipamientoInvalidoException;
import equipamiento.MallaDeEspinas;
import equipamiento.RostroEspiritual;
import escuelas.CatalogoEscuelas;
import escuelas.Escuela;
import tecnicas.CatalogoTecnicas;
import torneo.InscripcionInvalidaException;
import torneo.Torneo;

public class TorneoDeLosInmortales {

	private static final HeraldoImperial HERALDO = new HeraldoImperial();

	private static final Escuela SECTA_DEL_LOTO_CARMESI = new Escuela(
			"Secta del Loto Carmesí",
			List.of(CatalogoTecnicas.RUGIDO_DE_LA_BESTIA_PRIMORDIAL,
					CatalogoTecnicas.MEDITACION_DEL_INMORTAL),
			List.of(new AnilloDelArcangel(), new RostroEspiritual()));

	private static final Escuela TORRE_DE_LA_NIEBLA = new Escuela(
			"Torre de la Niebla",
			List.of(CatalogoTecnicas.CORTE_DE_LOS_MIL_AMANECERES,
					CatalogoTecnicas.MURO_DEL_CUERPO_DORADO),
			List.of(new BaculoDelVacio(), new MallaDeEspinas()));

	public static void main(String[] args) {
		escenarioTorneoCompleto();
		escenarioTorneoDeOchoParejas();
		escenarioReglasDelEquipamiento();
		escenarioInscripcionesInvalidas();
		escenarioEfectosTemporales();
	}

	private static void escenarioTorneoCompleto() {
		HERALDO.anunciarEscenario("Escenario 1: torneo completo de 4 parejas");

		CreadorCombatientes creador = new CreadorCombatientes();
		Combatiente aurelio = creador.crear(TipoCombatiente.CULTIVADOR_DE_LA_ESPADA,
				CatalogoEscuelas.ESPADA_CELESTIAL, "Aurelio");
		Combatiente nebula = creador.crear(TipoCombatiente.HECHICERO_DEL_VACIO,
				CatalogoEscuelas.PALACIO_DEL_VACIO, "Nébula");
		Combatiente kael = creador.crear(TipoCombatiente.MONJE_DEL_CUERPO_CELESTIAL,
				CatalogoEscuelas.MONASTERIO_DEL_CUERPO_DORADO, "Kael");
		Combatiente darian = creador.crear(TipoCombatiente.CULTIVADOR_DE_LA_ESPADA,
				CatalogoEscuelas.ESPADA_CELESTIAL, "Darian");
		Combatiente lian = creador.crear(TipoCombatiente.HECHICERO_DEL_VACIO,
				SECTA_DEL_LOTO_CARMESI, "Lian");
		Combatiente mei = creador.crear(TipoCombatiente.MONJE_DEL_CUERPO_CELESTIAL,
				TORRE_DE_LA_NIEBLA, "Mei");
		Combatiente tao = creador.crear(TipoCombatiente.CULTIVADOR_DE_LA_ESPADA,
				TORRE_DE_LA_NIEBLA, "Tao");
		Combatiente yun = creador.crear(TipoCombatiente.HECHICERO_DEL_VACIO,
				CatalogoEscuelas.MONASTERIO_DEL_CUERPO_DORADO, "Yun");

		CronistaDeLaSecta cronista = new CronistaDeLaSecta();
		Torneo torneo = new Torneo();
		torneo.agregarInteresado(HERALDO);
		torneo.agregarInteresado(cronista);

		torneo.inscribir(aurelio, nebula);
		torneo.inscribir(kael, darian);
		torneo.inscribir(lian, mei);
		torneo.inscribir(tao, yun);
		torneo.iniciar();

		HERALDO.separar();
		HERALDO.presentarTabla(cronista.getTablaDePosiciones());
	}

	private static void escenarioTorneoDeOchoParejas() {
		HERALDO.anunciarEscenario("Escenario 2: torneo de 8 parejas, solo el resultado final");

		CreadorCombatientes creador = new CreadorCombatientes();
		CronistaDeLaSecta cronista = new CronistaDeLaSecta();
		Torneo torneo = new Torneo();
		torneo.agregarInteresado(cronista);

		inscribirPareja(torneo, creador, "Aiden", "Brenna", CatalogoEscuelas.ESPADA_CELESTIAL);
		inscribirPareja(torneo, creador, "Cael", "Dara", CatalogoEscuelas.PALACIO_DEL_VACIO);
		inscribirPareja(torneo, creador, "Eron", "Fenna", CatalogoEscuelas.MONASTERIO_DEL_CUERPO_DORADO);
		inscribirPareja(torneo, creador, "Garrick", "Hanna", SECTA_DEL_LOTO_CARMESI);
		inscribirPareja(torneo, creador, "Ilan", "Jora", TORRE_DE_LA_NIEBLA);
		inscribirPareja(torneo, creador, "Kiran", "Lena", CatalogoEscuelas.ESPADA_CELESTIAL);
		inscribirPareja(torneo, creador, "Mavro", "Nira", CatalogoEscuelas.PALACIO_DEL_VACIO);
		inscribirPareja(torneo, creador, "Orin", "Pela", CatalogoEscuelas.MONASTERIO_DEL_CUERPO_DORADO);

		Pareja campeona = torneo.iniciar();

		HERALDO.anunciar("Campeona: pareja " + campeona.getIdentificador() + " ("
				+ campeona.getIntegrante1().getNombre() + " y "
				+ campeona.getIntegrante2().getNombre() + ")");
		HERALDO.presentarTabla(cronista.getTablaDePosiciones());
	}

	private static void escenarioReglasDelEquipamiento() {
		HERALDO.anunciarEscenario("Escenario 3: reglas del equipamiento");

		Combatiente kael = new CreadorCombatientes().crear(TipoCombatiente.MONJE_DEL_CUERPO_CELESTIAL,
				CatalogoEscuelas.MONASTERIO_DEL_CUERPO_DORADO, "Kael");
		Equipamiento anillo = new AnilloDelArcangel();
		anunciarEstado("sale del Monasterio", kael);

		HERALDO.separar();
		intentarEquipar(kael, anillo, "un tercer objeto");
		intentarEquipar(kael, new BaculoDelVacio(), "un cuarto objeto");

		HERALDO.separar();
		kael.quitar(anillo);
		anunciarEstado("se quita el Anillo del Arcangel", kael);
		intentarEquipar(kael, new MallaDeEspinas(), "una segunda armadura");

		HERALDO.separar();
		kael.restaurarParaCombate();
		anunciarEstado("recupera la vida al máximo", kael);
		kael.quitar(objetoDeCategoria(kael, Categoria.ARMADURA));
		anunciarEstado("se quita la armadura", kael);
	}

	private static void escenarioInscripcionesInvalidas() {
		HERALDO.anunciarEscenario("Escenario 4: inscripciones inválidas");

		CreadorCombatientes creador = new CreadorCombatientes();
		Combatiente aurelio = creador.crear(TipoCombatiente.CULTIVADOR_DE_LA_ESPADA,
				CatalogoEscuelas.ESPADA_CELESTIAL, "Aurelio");
		Combatiente nebula = creador.crear(TipoCombatiente.HECHICERO_DEL_VACIO,
				CatalogoEscuelas.PALACIO_DEL_VACIO, "Nébula");
		Combatiente kael = creador.crear(TipoCombatiente.MONJE_DEL_CUERPO_CELESTIAL,
				CatalogoEscuelas.MONASTERIO_DEL_CUERPO_DORADO, "Kael");

		Torneo torneo = new Torneo();
		Pareja primera = torneo.inscribir(aurelio, nebula);

		intentarInscribir("la misma pareja dos veces", () -> torneo.inscribir(primera));
		intentarInscribir("un combatiente ya inscripto", () -> torneo.inscribir(aurelio, kael));

		HERALDO.separar();
		intentarIniciar(torneo);
	}

	private static void escenarioEfectosTemporales() {
		HERALDO.anunciarEscenario("Escenario 5: efectos temporales (veneno y regeneración)");

		Combatiente aurelio = new CreadorCombatientes().crear(TipoCombatiente.CULTIVADOR_DE_LA_ESPADA,
				CatalogoEscuelas.ESPADA_CELESTIAL, "Aurelio");
		anunciarVida("empieza con", aurelio);

		HERALDO.separar();
		aurelio.agregarEfecto(new EfectoTemporal(aurelio, new EstadoVeneno()));
		HERALDO.anunciar("Aurelio es envenenado y llega un segundo veneno con el primero activo (se ignora)");
		aurelio.agregarEfecto(new EfectoTemporal(aurelio, new EstadoVeneno()));
		aurelio.aplicarEfectosTemporales();
		anunciarVida("en su turno sufre un solo veneno", aurelio);
		aurelio.aplicarEfectosTemporales();
		anunciarVida("en el turno siguiente no sufre nada, el veneno terminó", aurelio);

		HERALDO.separar();
		aurelio.agregarEfecto(new EfectoTemporal(aurelio, new EstadoRegeneracion()));
		aurelio.aplicarEfectosTemporales();
		anunciarVida("recibe Regeneración, cura 40 y vuelve a su máximo", aurelio);
	}

	private static void anunciarVida(String momento, Combatiente combatiente) {
		HERALDO.anunciar(combatiente.getNombre() + " " + momento + ": vida "
				+ combatiente.getVidaActual() + "/" + combatiente.getVidaMaxima());
	}

	private static void inscribirPareja(Torneo torneo, CreadorCombatientes creador,
			String nombreDelCultivador, String nombreDelMonje, Escuela escuela) {
		Combatiente cultivador = creador.crear(TipoCombatiente.CULTIVADOR_DE_LA_ESPADA, escuela,
				nombreDelCultivador);
		Combatiente monje = creador.crear(TipoCombatiente.MONJE_DEL_CUERPO_CELESTIAL, escuela,
				nombreDelMonje);
		torneo.inscribir(cultivador, monje);
	}

	private static void intentarEquipar(Combatiente combatiente, Equipamiento objeto, String descripcion) {
		try {
			combatiente.equipar(objeto);
			anunciarEstado("equipa " + objeto.getNombre(), combatiente);
		} catch (EquipamientoInvalidoException e) {
			HERALDO.anunciar("Rechazado (" + descripcion + "): " + e.getMessage());
			anunciarEstado("queda sin cambios", combatiente);
		}
	}

	private static void intentarInscribir(String descripcion, Runnable inscripcion) {
		try {
			inscripcion.run();
		} catch (InscripcionInvalidaException e) {
			HERALDO.anunciar("Rechazado (" + descripcion + "): " + e.getMessage());
		}
	}

	private static void intentarIniciar(Torneo torneo) {
		try {
			torneo.iniciar();
		} catch (InscripcionInvalidaException e) {
			HERALDO.anunciar("Rechazado (iniciar con una sola pareja): " + e.getMessage());
		}
	}

	private static Equipamiento objetoDeCategoria(Combatiente combatiente, Categoria categoria) {
		for (Equipamiento objeto : combatiente.getEquipamiento()) {
			if (objeto.getCategoria() == categoria) {
				return objeto;
			}
		}
		throw new IllegalStateException(combatiente.getNombre() + " no lleva un objeto de categoría " + categoria);
	}

	private static void anunciarEstado(String momento, Combatiente combatiente) {
		List<String> objetos = new ArrayList<>();
		for (Equipamiento objeto : combatiente.getEquipamiento()) {
			objetos.add(objeto.getNombre());
		}
		HERALDO.anunciar(combatiente.getNombre() + " " + momento + ": vida "
				+ combatiente.getVidaActual() + "/" + combatiente.getVidaMaxima()
				+ ", objetos " + objetos);
	}
}
