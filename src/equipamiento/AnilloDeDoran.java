package equipamiento;

public class AnilloDeDoran extends Equipamiento{
	public AnilloDeDoran() {
		super("Anillo de Doran", Categoria.ANILLO);
	}
	
	@Override
	public int bonusEnergiaMaxima() { return 50; }
}
