package equipamiento;

public class MallaDeEspinas extends Equipamiento{
	public MallaDeEspinas() {
		super("Malla de Espinas", Categoria.ARMADURA);
	}

	@Override
	public int reduccionDmg() { return 10; }
}
