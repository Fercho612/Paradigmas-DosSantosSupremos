package efectosTemporales;

public interface EstadoEfecto{
    void aplicar (EfectoTemporal efecto);
    boolean estaActivo();
}