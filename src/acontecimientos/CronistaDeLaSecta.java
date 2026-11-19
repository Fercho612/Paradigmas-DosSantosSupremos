package acontecimientos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cronista de la Secta: registra los acontecimientos y permite consultar el
 * historial. Con los resultados de los combates arma la tabla de
 * posiciones.
 */
public class CronistaDeLaSecta implements Interesado {

	/** Historial en el orden en que ocurrieron los acontecimientos. */
	private final List<Acontecimiento> historial = new ArrayList<>();

	/** Última posición conocida de cada pareja, por identificador. */
	private final Map<Integer, Posicion> posiciones = new HashMap<>();

	@Override
	public void notificar(Acontecimiento acontecimiento) {
		historial.add(acontecimiento);
		acontecimiento.registrarEn(this);
	}

	/**
	 * Actualiza la posición de las dos parejas del combate. Un resultado
	 * posterior reemplaza al anterior, así queda la última ronda jugada.
	 *
	 * @param resultado el resultado del combate
	 */
	public void registrarResultado(ResultadoCombate resultado) {
		posiciones.put(Integer.valueOf(resultado.getIdGanadora()), new Posicion(
				resultado.getIdGanadora(), resultado.getRonda(), resultado.getVidaGanadora()));
		posiciones.put(Integer.valueOf(resultado.getIdPerdedora()), new Posicion(
				resultado.getIdPerdedora(), resultado.getRonda(), resultado.getVidaPerdedora()));
	}

	/**
	 * @return copia no modificable del historial
	 */
	public List<Acontecimiento> getHistorial() {
		return List.copyOf(historial);
	}

	/**
	 * Devuelve la tabla de posiciones: las parejas ordenadas por la última
	 * ronda que jugaron (mayor primero), en empate por la vida total final
	 * (mayor primero) y luego por identificador (menor primero).
	 *
	 * @return la tabla ordenada
	 */
	public List<Posicion> getTablaDePosiciones() {
		List<Posicion> tabla = new ArrayList<>(posiciones.values());
		tabla.sort(Comparator
				.comparingInt(Posicion::getUltimaRonda).reversed()
				.thenComparing(Comparator.comparingInt(Posicion::getVidaTotalFinal).reversed())
				.thenComparingInt(Posicion::getIdPareja));
		return tabla;
	}
}
