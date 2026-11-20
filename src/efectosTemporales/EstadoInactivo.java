package efectosTemporales;

public class EstadoInactivo implements EstadoEfecto {

    @Override
    public void aplicar(EfectoTemporal efecto) {

    }
    @Override
    public boolean estaActivo() {
        return false;
    }
}