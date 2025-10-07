package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_06EstacionesConDisponibilidad {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // ====== ERRORES (n <= 1) ======
    @Test
    public void estacionesConDisponibilidadError01_nIgual1() {
        retorno = s.estacionesConDisponibilidad(1);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void estacionesConDisponibilidadError01_ceroYNegativo() {
        retorno = s.estacionesConDisponibilidad(0);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.estacionesConDisponibilidad(-3);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    // ====== OK: sin estaciones ======
    @Test
    public void estacionesConDisponibilidadOk_sinEstaciones() {
        retorno = s.estacionesConDisponibilidad(2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(0, retorno.getValorEntero());
    }

    // ====== OK: umbral estricto (ocupación > n) ======
    @Test
    public void estacionesConDisponibilidadOk_umbralEstricto() {
        // Estaciones
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("A", "BarrioA", 10).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("B", "BarrioB", 10).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("C", "BarrioC", 10).getResultado());

        // Bicis (6 caracteres)
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("AA0001", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("AA0002", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("AA0003", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("AA0004", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("AA0005", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("BB0001", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("BB0002", "Urbana").getResultado());

        // Ocupaciones: A=5, B=2, C=0
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("AA0001", "A").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("AA0002", "A").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("AA0003", "A").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("AA0004", "A").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("AA0005", "A").getResultado());

        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("BB0001", "B").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("BB0002", "B").getResultado());

        retorno = s.estacionesConDisponibilidad(2); // ocupación > 2 → sólo A
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorEntero());
    }

    @Test
    public void estacionesConDisponibilidadOk_variosUmbrales() {
        // Estaciones
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("X", "BarrioX", 10).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("Y", "BarrioY", 10).getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarEstacion("Z", "BarrioZ", 10).getResultado());

        // Bicis con 6 caracteres (antes fallaba por esto)
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("XX0001", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("XX0002", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("XX0003", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("YY0001", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("ZZ0001", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("ZZ0002", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("ZZ0003", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK, s.registrarBicicleta("ZZ0004", "Urbana").getResultado());

        // Ocupaciones: X=3, Y=1, Z=4
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("XX0001", "X").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("XX0002", "X").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("XX0003", "X").getResultado());

        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("YY0001", "Y").getResultado());

        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("ZZ0001", "Z").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("ZZ0002", "Z").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("ZZ0003", "Z").getResultado());
        assertEquals(Retorno.Resultado.OK, s.asignarBicicletaAEstacion("ZZ0004", "Z").getResultado());

        // n = 3 → ocupación > 3 → sólo Z (4)
        retorno = s.estacionesConDisponibilidad(3);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(1, retorno.getValorEntero());

        // n = 2 → ocupación > 2 → X (3) y Z (4)
        retorno = s.estacionesConDisponibilidad(2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(2, retorno.getValorEntero());
    }
}
