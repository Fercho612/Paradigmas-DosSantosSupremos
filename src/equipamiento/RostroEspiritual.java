package equipamiento;

public class RostroEspiritual extends Equipamiento{
	public RostroEspiritual() {
		super("Rostro Espiritual", Categoria.SELLO);
	}

	@Override
	public int bonusVidaMax() { return 150; }
}
