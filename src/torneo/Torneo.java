package torneo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import acontecimientos.Acontecimiento;
import acontecimientos.Interesado;
import acontecimientos.Proclamacion;
import combate.Combate;
import combate.Pareja;
import combatientes.Combatiente;

/**
 * Torneo eliminatorio por parejas.
 * <p>
 * Las parejas se inscriben en orden y el torneo se juega solo con 4 u 8.
 * Los cruces de la primera ronda se arman por orden de inscripción (primera
 * contra segunda, tercera contra cuarta, etc.); en las siguientes, las
 * ganadoras se cruzan en el orden de los cruces de la ronda anterior. El
 * torneo termina cuando queda una sola pareja.
 * </p>
 * <p>
 * Los acontecimientos se comunican a los interesados registrados (patrón
 * Observer), sin que el torneo conozca qué hace cada uno con ellos.
 * </p>
 */
public class Torneo {

	private final List<Pareja> parejas = new ArrayList<>();
	private final Set<Combatiente> inscriptos = new HashSet<>();
	private final List<Interesado> interesados = new ArrayList<>();
	private Pareja campeona;

	public void agregarInteresado(Interesado interesado) {
		interesados.add(interesado);
	}

	public void inscribir(Pareja pareja) {
		if (parejas.contains(pareja)) {
			throw new InscripcionInvalidaException(
					"La pareja " + pareja.getIdentificador() + " ya está inscripta");
		}
		for (Combatiente c : pareja.getIntegrantes()) {
			if (inscriptos.contains(c)) {
				throw new InscripcionInvalidaException(
						"El combatiente '" + c.getNombre() + "' ya está inscripto");
			}
		}
		parejas.add(pareja);
		inscriptos.addAll(pareja.getIntegrantes());
	}

	public Pareja iniciar() {
		if (parejas.size() != 4 && parejas.size() != 8) {
			throw new InscripcionInvalidaException(
					"El torneo solo puede jugarse con 4 u 8 parejas (hay " + parejas.size() + ")");
		}

		for (Combatiente c : inscriptos) {
			c.reiniciarParaTorneo();
		}

		List<Pareja> enJuego = new ArrayList<>(parejas);
		int ronda = 1;
		while (enJuego.size() > 1) {
			List<Pareja> ganadoras = new ArrayList<>();
			for (int i = 0; i < enJuego.size(); i += 2) {
				Combate combate = new Combate(enJuego.get(i), enJuego.get(i + 1), ronda, interesados);
				ganadoras.add(combate.ejecutar());
			}
			enJuego = ganadoras;
			ronda++;
		}

		campeona = enJuego.get(0);
		notificar(new Proclamacion(campeona.getIdentificador(),
				campeona.getIntegrante1().getNombre(), campeona.getIntegrante2().getNombre()));
		return campeona;
	}

	private void notificar(Acontecimiento acontecimiento) {
		for (Interesado interesado : interesados) {
			interesado.notificar(acontecimiento);
		}
	}

	public Pareja getCampeona() {
		return campeona;
	}

	public List<Pareja> getParejas() {
		return List.copyOf(parejas);
	}
}
