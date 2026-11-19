package efectosTemporales;

public class EstadoRegeneracion implements EstadoEfecto {

    @Override
public void aplicar(EfectoTemporal efecto) {
    efecto.getCombatiente().curar(40);
    efecto.finalizar();
}
}