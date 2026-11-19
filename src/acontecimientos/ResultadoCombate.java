package acontecimientos;

/**
 * Resultado de un combate: ronda, pareja ganadora y vida total de cada
 * pareja al terminar.
 */
public class ResultadoCombate extends Acontecimiento {

	private final int ronda;
	private final int idGanadora;
	private final int vidaGanadora;
	private final int idPerdedora;
	private final int vidaPerdedora;

	public ResultadoCombate(int ronda, int idGanadora, int vidaGanadora,
			int idPerdedora, int vidaPerdedora) {
		this.ronda = ronda;
		this.idGanadora = idGanadora;
		this.vidaGanadora = vidaGanadora;
		this.idPerdedora = idPerdedora;
		this.vidaPerdedora = vidaPerdedora;
	}

	public int getRonda() {
		return ronda;
	}

	public int getIdGanadora() {
		return idGanadora;
	}

	public int getVidaGanadora() {
		return vidaGanadora;
	}

	public int getIdPerdedora() {
		return idPerdedora;
	}

	public int getVidaPerdedora() {
		return vidaPerdedora;
	}

	@Override
	public void registrarEn(CronistaDeLaSecta cronista) {
		cronista.registrarResultado(this);
	}

	@Override
	public String getDescripcion() {
		return "Ronda " + ronda + ": gana la pareja " + idGanadora
				+ " (vida total: " + vidaGanadora + ") contra la pareja " + idPerdedora
				+ " (vida total: " + vidaPerdedora + ")";
	}
}
