package efectosTemporales;

public class EstadoVeneno implements EstadoEfecto {

    @Override
public void aplicar(EfectoTemporal efecto) {
    efecto.getCombatiente().recibirDanio(40);
    efecto.finalizar();
}
}