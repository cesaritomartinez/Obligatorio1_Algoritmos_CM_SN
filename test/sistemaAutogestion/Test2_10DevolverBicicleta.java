package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test2_10DevolverBicicleta {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // Preparar un escenario básico con un alquiler activo
    private void prepararAlquilerSimple() {
        // Usuario
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("12345678", "Ana").getResultado());

        // Estaciones
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", 2).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Parque", "Parque Rodo", 2).getResultado());

        // Bicicleta y asignación a estación origen
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AB1234", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("AB1234", "Centro").getResultado());

        // Alquiler desde la estación Centro
        assertEquals(Retorno.Resultado.OK,
                s.alquilarBicicleta("12345678", "Centro").getResultado());
    }

    @Test
    public void devolverBicicleta_OK_conAnclajeLibre() {
        prepararAlquilerSimple();

        // Devolver en estación con anclaje libre
        retorno = s.devolverBicicleta("12345678", "Parque");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Verificamos que el ranking refleje 1 uso de URBANA
        Retorno rRanking = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, rRanking.getResultado());

        String ranking = rRanking.getValorString();
        assertNotNull(ranking);
        // ranking tiene el formato: "ELECTRICA#x|MOUNTAIN#y|URBANA#z"
        assertTrue("Se esperaba ver URBANA#1 en el ranking pero fue: " + ranking,
                   ranking.contains("URBANA#1"));
    }

    @Test
    public void devolverBicicleta_ERROR1_parametrosVacios() {
        retorno = s.devolverBicicleta("", "Parque");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.devolverBicicleta("12345678", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void devolverBicicleta_ERROR2_usuarioInexistenteOSinAlquiler() {
        // Estación destino existe, pero usuario no
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Parque", "Parque Rodo", 2).getResultado());

        retorno = s.devolverBicicleta("12345678", "Parque");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());

        // Usuario existe pero sin alquiler activo → también ERROR_2
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("12345678", "Ana").getResultado());

        retorno = s.devolverBicicleta("12345678", "Parque");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    public void devolverBicicleta_ERROR3_estacionDestinoInexistente() {
        prepararAlquilerSimple();

        // Estación destino no existe → ERROR_3
        retorno = s.devolverBicicleta("12345678", "NoExiste");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
}
