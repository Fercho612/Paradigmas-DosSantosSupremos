package equipamiento;

public class DanzaDeLaMuerte extends Equipamiento{
	public DanzaDeLaMuerte() {
		super("Danza de la Muerte", Categoria.ARMA);
	}

	@Override
	public int bonusDanio() { return 50; }
}
