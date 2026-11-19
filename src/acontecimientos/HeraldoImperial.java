package acontecimientos;

import java.util.List;

/**
 * Heraldo Imperial: presenta los acontecimientos por pantalla.
 * Es la única clase del sistema que escribe en pantalla.
 */
public class HeraldoImperial implements Interesado {

	@Override
	public void notificar(Acontecimiento acontecimiento) {
		System.out.println(acontecimiento.getDescripcion());
	}

	public void presentarTabla(List<Posicion> tabla) {
		System.out.println("Tabla de posiciones:");
		for (int i = 0; i < tabla.size(); i++) {
			System.out.println((i + 1) + ". " + tabla.get(i));
		}
	}
}
