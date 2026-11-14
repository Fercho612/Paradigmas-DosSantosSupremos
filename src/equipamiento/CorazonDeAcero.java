package equipamiento;

public class CorazonDeAcero extends Equipamiento{
	public CorazonDeAcero() {
		super("Corazon de Acero", Categoria.ARMADURA);
	}
	
	@Override
	public int bonusVidaMax() { return 300; }
}
