package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_08AsignarBicicletaAEstacion {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void asignarBicicletaAEstacionOk() {
        // Estación y bici válidas → asigna OK
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", 2).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AB1234", "Urbana").getResultado());

        retorno = s.asignarBicicletaAEstacion("AB1234", "Centro");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    public void asignarBicicletaAEstacionError01() {
        // Parámetros vacíos → ERROR_1
        retorno = s.asignarBicicletaAEstacion("", "Centro");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.asignarBicicletaAEstacion("AB1234", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void asignarBicicletaAEstacionError02_biciInexistenteONoDisponible() {
        // Bici inexistente → ERROR_2
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Cordón", "Cordón", 2).getResultado());

        retorno = s.asignarBicicletaAEstacion("NOEXST", "Cordón");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        // Bici registrada pero NO disponible (ej.: en mantenimiento) → ERROR_2
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("CD5678", "Electrica").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.marcarEnMantenimiento("CD5678", "freno").getResultado());

        retorno = s.asignarBicicletaAEstacion("CD5678", "Cordón");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void asignarBicicletaAEstacionError03_estacionInexistente() {
        // Estación no existe → ERROR_3
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("EF0001", "Mountain").getResultado());

        retorno = s.asignarBicicletaAEstacion("EF0001", "NoExiste");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    public void asignarBicicletaAEstacionError04_estacionLlena() {
        // Capacidad 1: primera asignación OK, segunda → ERROR_4
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Parque", "Parque Rodo", 1).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("GH1111", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("GH2222", "Urbana").getResultado());

        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("GH1111", "Parque").getResultado());

        retorno = s.asignarBicicletaAEstacion("GH2222", "Parque");
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }
}
