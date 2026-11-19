package acontecimientos;

/**
 * Interesado en los acontecimientos del combate y del torneo (patrón
 * Observer). Para sumar un interesado nuevo basta con implementar esta
 * interfaz y registrarlo en el torneo, sin modificar su lógica.
 */
public interface Interesado {

	void notificar(Acontecimiento acontecimiento);
}
