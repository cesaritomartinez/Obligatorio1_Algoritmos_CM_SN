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

    private void prepararAlquilerSimple() {
        // Usuario, estación origen, estación destino y bici con alquiler activo
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("12345678", "Ana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Centro", "Centro", 2).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("Parque", "Parque Rodo", 2).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("AB1234", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("AB1234", "Centro").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.alquilarBicicleta("12345678", "Centro").getResultado());
    }

    @Test
    public void devolverBicicletaOk() {
        prepararAlquilerSimple();

        retorno = s.devolverBicicleta("12345678", "Parque");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Opcional: verificar que el ranking refleje un uso de Urbana
        Retorno rRanking = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, rRanking.getResultado());
        assertTrue(rankingContieneUsoUrbana(rRanking.getValorString(), 1));
    }

    private boolean rankingContieneUsoUrbana(String ranking, int usosEsperados) {
        // ranking: "Electrica#x|Mountain#y|Urbana#z"
        return ranking != null && ranking.contains("Urbana#" + usosEsperados);
    }

    @Test
    public void devolverBicicletaError01_parametrosVacios() {
        retorno = s.devolverBicicleta("", "Parque");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());

        retorno = s.devolverBicicleta("12345678", "");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    public void devolverBicicletaError02_usuarioInexistenteOSinAlquiler() {
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
    public void devolverBicicletaError03_estacionDestinoInexistente() {
        prepararAlquilerSimple();

        // Estación destino no existe → ERROR_3
        retorno = s.devolverBicicleta("12345678", "NoExiste");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }
}
