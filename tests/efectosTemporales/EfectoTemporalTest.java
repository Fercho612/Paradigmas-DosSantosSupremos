
package efectosTemporales;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import combatientes.Combatiente;
import combatientes.CreadorCombatientes;
import combatientes.TipoCombatiente;
import escuelas.CatalogoEscuelas;
import tecnicas.CatalogoTecnicas;

class EfectoTemporalTest {

    @Test
    void elVenenoResta40PuntosDeVida() {
        Combatiente combatiente = new CreadorCombatientes().crear(
                TipoCombatiente.HECHICERO_DEL_VACIO,
                CatalogoEscuelas.PALACIO_DEL_VACIO, "H");

        int vidaInicial = combatiente.getVidaActual();

        EfectoTemporal veneno = new EfectoTemporal(combatiente, new EstadoVeneno());
        veneno.aplicar();

        assertEquals(vidaInicial - 40, combatiente.getVidaActual());
    }

    @Test
    void laRegeneracionRecupera40PuntosDeVida() {
        Combatiente combatiente = new CreadorCombatientes().crear(
                TipoCombatiente.HECHICERO_DEL_VACIO,
                CatalogoEscuelas.PALACIO_DEL_VACIO, "H");

        combatiente.recibirDanio(100);
        int vidaInicial = combatiente.getVidaActual();

        EfectoTemporal regeneracion =
                new EfectoTemporal(combatiente, new EstadoRegeneracion());
        regeneracion.aplicar();

        assertEquals(vidaInicial + 40, combatiente.getVidaActual());
    }

    @Test
    void elEfectoNoSeAplicaDosVeces() {
        Combatiente combatiente = new CreadorCombatientes().crear(
                TipoCombatiente.HECHICERO_DEL_VACIO,
                CatalogoEscuelas.PALACIO_DEL_VACIO, "H");

        int vidaInicial = combatiente.getVidaActual();

        EfectoTemporal veneno = new EfectoTemporal(combatiente, new EstadoVeneno());
        veneno.aplicar();
        veneno.aplicar();

        assertEquals(vidaInicial - 40, combatiente.getVidaActual());
    }
    @Test
    void tecnicaVenenoAplicaEfectoAlObjetivo() {
        Combatiente ejecutor = new CreadorCombatientes().crear(
                TipoCombatiente.HECHICERO_DEL_VACIO,
                CatalogoEscuelas.PALACIO_DEL_VACIO, "Ejecutor");

        Combatiente objetivo = new CreadorCombatientes().crear(
                TipoCombatiente.HECHICERO_DEL_VACIO,
                CatalogoEscuelas.PALACIO_DEL_VACIO, "Objetivo");

        int vidaInicial = objetivo.getVidaActual();

        CatalogoTecnicas.VENENO.ejecutar(ejecutor, objetivo);
        objetivo.aplicarEfectosTemporales();

        assertEquals(vidaInicial - 40, objetivo.getVidaActual());
    }
    @Test
    void tecnicaRegeneracionAplicaEfectoAlEjecutor() {
        Combatiente ejecutor = new CreadorCombatientes().crear(
                TipoCombatiente.HECHICERO_DEL_VACIO,
                CatalogoEscuelas.PALACIO_DEL_VACIO, "Ejecutor");

        ejecutor.recibirDanio(100);
        int vidaInicial = ejecutor.getVidaActual();

        CatalogoTecnicas.REGENERACION.ejecutar(ejecutor, ejecutor);
        ejecutor.aplicarEfectosTemporales();

        assertEquals(vidaInicial + 40, ejecutor.getVidaActual());
    }
    @Test
    void noPermiteAgregarDosEfectosActivos() {
        Combatiente combatiente = new CreadorCombatientes().crear(
                TipoCombatiente.HECHICERO_DEL_VACIO,
                CatalogoEscuelas.PALACIO_DEL_VACIO, "Combatiente");

        int vidaInicial = combatiente.getVidaActual();

        combatiente.agregarEfecto(
                new EfectoTemporal(combatiente, new EstadoVeneno()));

        combatiente.agregarEfecto(
                new EfectoTemporal(combatiente, new EstadoRegeneracion()));

        combatiente.aplicarEfectosTemporales();

        assertEquals(vidaInicial - 40, combatiente.getVidaActual());
    }
}
