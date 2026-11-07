package combatientes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import equipamiento.Equipamiento;
import equipamiento.EquipamientoInvalidoException;

public abstract class Combatiente {
	private final int identificador;
	protected final String nombre;
	private final int vidaMax, energiaMax;
	private int vidaAct, energiaAct;
	private final List<Equipamiento> equipamiento = new ArrayList<>();
	private final int MAX_EQUIPAMIENTOS = 3;
	
	protected Combatiente(int identificador, String nombre, int vidaMax, int energiaMax) {
		this.identificador = identificador;
		this.nombre = nombre;
		this.vidaMax = vidaMax;
		this.vidaAct = vidaMax;
		this.energiaMax = energiaMax;
		this.energiaAct = energiaMax;
	}

	public void equipar(Equipamiento objeto) {
		if (equipamiento.size() >= MAX_EQUIPAMIENTOS) {
			throw new EquipamientoInvalidoException("No puede llevar más de " + MAX_EQUIPAMIENTOS + " objetos");
		}
		for (Equipamiento e : equipamiento) {
			if (e.getCategoria() == objeto.getCategoria()) {
				throw new EquipamientoInvalidoException("Ya lleva un objeto de categoría " + objeto.getCategoria());
			}
		}
		equipamiento.add(objeto);
	}

	public void quitar(Equipamiento objeto) {
		if (!this.equipamiento.remove(objeto)) {
			throw new EquipamientoInvalidoException("No lleva " + objeto.getNombre());
		}
		this.vidaAct = Math.min(vidaAct, getVidaMaxima());
		this.energiaAct = Math.min(energiaAct, getEnergiaMaxima());
	}

	public List<Equipamiento> getEquipamiento() {
		return List.copyOf(equipamiento);
	}
	
	public int getVidaMaxima() {
		int total = this.vidaMax;
		for (Equipamiento e : this.equipamiento) {
			total += e.bonusVidaMaxima();
		}
		return total;
	}

	public int getEnergiaMaxima() {
		int total = this.energiaMax;
		for (Equipamiento e : this.equipamiento) {
			total += e.bonusEnergiaMaxima();
		}
		return total;
	}

	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(identificador));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (!(obj instanceof Combatiente))
			return false;
		
		Combatiente other = (Combatiente) obj;
		return identificador == other.identificador;
	}

}
