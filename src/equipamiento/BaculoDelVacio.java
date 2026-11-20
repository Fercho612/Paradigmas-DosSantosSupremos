package equipamiento;

public class BaculoDelVacio extends Equipamiento{
	public BaculoDelVacio() {
		super("Baculo del Vacio", Categoria.ARMA);
	}

	@Override
	public int bonusEnergiaMax() { return 100; }
}
