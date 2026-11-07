package tecnicas;

import combatientes.Combatiente;

@FunctionalInterface
public interface Efecto {
	void aplicar(final Combatiente ejecutor, final Combatiente objetivo);
}
