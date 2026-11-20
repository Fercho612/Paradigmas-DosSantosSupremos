package equipamiento;

public class AnilloDelArcangel extends Equipamiento{
	public AnilloDelArcangel() {
		super("Anillo del Arcangel", Categoria.ANILLO);
	}

	@Override
	public int bonusDmg() { return 10; }
}
