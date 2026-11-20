package escuelas;

import java.util.List;

import equipamiento.AnilloDeDoran;
import equipamiento.CorazonDeAcero;
import equipamiento.DanzaDeLaMuerte;
import equipamiento.VeloDeBanshee;
import tecnicas.CatalogoTecnicas;

/**
 * Las tres escuelas base del torneo, definidas como objetos {@link Escuela}.
 * Cada una refleja su filosofía a través de las técnicas y el equipamiento
 * que entrega. El orden de las técnicas es el orden de preferencia de la
 * escuela ante un empate.
 */
public final class CatalogoEscuelas {

	/** Ataques físicos y armas. */
	public static final Escuela ESPADA_CELESTIAL = new Escuela(
			"Escuela de la Espada Celestial",
			List.of(CatalogoTecnicas.CORTE_DE_LOS_MIL_AMANECERES,
					CatalogoTecnicas.RUGIDO_DE_LA_BESTIA_PRIMORDIAL),
			List.of(new DanzaDeLaMuerte()));

	/** Técnicas especiales y dominio de la energía. */
	public static final Escuela PALACIO_DEL_VACIO = new Escuela(
			"Palacio del Vacío",
			List.of(CatalogoTecnicas.DOMINIO_DE_LA_NADA,
					CatalogoTecnicas.CORTE_DE_LOS_MIL_AMANECERES,
					CatalogoTecnicas.VENENO),
			List.of(new AnilloDeDoran()));

	/** Resistencia, defensa y recuperación. */
	public static final Escuela MONASTERIO_DEL_CUERPO_DORADO = new Escuela(
			"Monasterio del Cuerpo Dorado",
			List.of(CatalogoTecnicas.MURO_DEL_CUERPO_DORADO,
					CatalogoTecnicas.MEDITACION_DEL_INMORTAL,
			        CatalogoTecnicas.REGENERACION),
			List.of(new CorazonDeAcero(), new VeloDeBanshee()));

	private CatalogoEscuelas() {
		// Clase de utilidad: no se instancia.
	}
}