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

	private final List<Acontecimiento> historial = new ArrayList<>();
	private final Map<Integer, Posicion> posiciones = new HashMap<>();

	@Override
	public void notificar(Acontecimiento acontecimiento) {
		historial.add(acontecimiento);
		acontecimiento.registrarEn(this);
	}

	public void registrarResultado(ResultadoCombate resultado) {
		posiciones.put(Integer.valueOf(resultado.getIdGanadora()), new Posicion(
				resultado.getIdGanadora(), resultado.getRonda(), resultado.getVidaGanadora()));
		posiciones.put(Integer.valueOf(resultado.getIdPerdedora()), new Posicion(
				resultado.getIdPerdedora(), resultado.getRonda(), resultado.getVidaPerdedora()));
	}

	public List<Acontecimiento> getHistorial() {
		return List.copyOf(historial);
	}

	public List<Posicion> getTablaDePosiciones() {
		List<Posicion> tabla = new ArrayList<>(posiciones.values());
		tabla.sort(Comparator
				.comparingInt(Posicion::getUltimaRonda).reversed()
				.thenComparing(Comparator.comparingInt(Posicion::getVidaTotalFinal).reversed())
				.thenComparingInt(Posicion::getIdPareja));
		return tabla;
	}
}
