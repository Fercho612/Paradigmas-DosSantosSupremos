package equipamiento;

public class VeloDeBanshee extends Equipamiento{
	public VeloDeBanshee() {
		super("Velo de Banshee", Categoria.SELLO);
	}
	
	@Override
	public int reduccionDmg() { return 20; }
}
