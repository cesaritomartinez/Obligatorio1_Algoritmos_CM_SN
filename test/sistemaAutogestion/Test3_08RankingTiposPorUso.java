package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_08RankingTiposPorUso {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    // --------- helpers ---------
    private String[] partesRanking(String ranking) {
        assertNotNull("El ranking no debe ser null", ranking);
        return ranking.split("\\|");
    }

    private String getTipo(String parte) {
        return parte.split("#")[0];
    }

    private int getUsos(String parte) {
        return Integer.parseInt(parte.split("#")[1]);
    }

    private void assertTipoYUsos(String parte, String tipoEsperado, int usosEsperados) {
        String tipo = getTipo(parte);
        int usos = getUsos(parte);
        assertEquals(tipoEsperado.toUpperCase(), tipo.toUpperCase());
        assertEquals(usosEsperados, usos);
    }
    // ----------------------------

    @Test
    public void rankingInicialSinUsos() {
        // Recién creado, ningún alquiler → todos 0
        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        String[] partes = partesRanking(retorno.getValorString());
        assertEquals(3, partes.length);

        // Con todos en 0, el orden debe ser alfabético: Electrica, Mountain, Urbana
        assertTipoYUsos(partes[0], "Electrica", 0);
        assertTipoYUsos(partes[1], "Mountain", 0);
        assertTipoYUsos(partes[2], "Urbana", 0);
    }

    @Test
    public void rankingConUsosDiferentes() {
        // Queremos:
        //   Urbana: 3 usos
        //   Electrica: 2 usos
        //   Mountain: 1 uso
        // → Esperado (por orden de uso desc): Urbana, Electrica, Mountain

        // ---------- URBANA x3 ----------
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("11111111", "U1").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("E_U", "E_U", 1).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("URB001", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("URB001", "E_U").getResultado());

        for (int i = 0; i < 3; i++) {
            assertEquals(Retorno.Resultado.OK,
                    s.alquilarBicicleta("11111111", "E_U").getResultado());
            assertEquals(Retorno.Resultado.OK,
                    s.devolverBicicleta("11111111", "E_U").getResultado());
        }

        // ---------- ELECTRICA x2 ----------
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("22222222", "E1").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("E_E", "E_E", 1).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("ELE001", "Electrica").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("ELE001", "E_E").getResultado());

        for (int i = 0; i < 2; i++) {
            assertEquals(Retorno.Resultado.OK,
                    s.alquilarBicicleta("22222222", "E_E").getResultado());
            assertEquals(Retorno.Resultado.OK,
                    s.devolverBicicleta("22222222", "E_E").getResultado());
        }

        // ---------- MOUNTAIN x1 ----------
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("33333333", "M1").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("E_M", "E_M", 1).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("MTN001", "Mountain").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("MTN001", "E_M").getResultado());

        assertEquals(Retorno.Resultado.OK,
                s.alquilarBicicleta("33333333", "E_M").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.devolverBicicleta("33333333", "E_M").getResultado());

        // ---------- Ver ranking ----------
        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        String[] partes = partesRanking(retorno.getValorString());
        assertEquals(3, partes.length);

        assertTipoYUsos(partes[0], "Urbana", 3);
        assertTipoYUsos(partes[1], "Electrica", 2);
        assertTipoYUsos(partes[2], "Mountain", 1);
    }

    @Test
    public void rankingConEmpate_porOrdenAlfabetico() {
        // Queremos:
        //   Electrica: 2 usos
        //   Mountain: 2 usos
        //   Urbana: 1 uso
        // Con empate en 2, gana alfabéticamente "Electrica" antes que "Mountain"
        // → Esperado: Electrica, Mountain, Urbana

        // ---------- ELECTRICA x2 ----------
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("44444444", "EUser").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("E_E2", "E_E2", 1).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("ELE002", "Electrica").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("ELE002", "E_E2").getResultado());

        for (int i = 0; i < 2; i++) {
            assertEquals(Retorno.Resultado.OK,
                    s.alquilarBicicleta("44444444", "E_E2").getResultado());
            assertEquals(Retorno.Resultado.OK,
                    s.devolverBicicleta("44444444", "E_E2").getResultado());
        }

        // ---------- MOUNTAIN x2 ----------
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("55555555", "MUser").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("E_M2", "E_M2", 1).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("MTN002", "Mountain").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("MTN002", "E_M2").getResultado());

        for (int i = 0; i < 2; i++) {
            assertEquals(Retorno.Resultado.OK,
                    s.alquilarBicicleta("55555555", "E_M2").getResultado());
            assertEquals(Retorno.Resultado.OK,
                    s.devolverBicicleta("55555555", "E_M2").getResultado());
        }

        // ---------- URBANA x1 ----------
        assertEquals(Retorno.Resultado.OK,
                s.registrarUsuario("66666666", "UUser").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarEstacion("E_U2", "E_U2", 1).getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.registrarBicicleta("URB002", "Urbana").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.asignarBicicletaAEstacion("URB002", "E_U2").getResultado());

        assertEquals(Retorno.Resultado.OK,
                s.alquilarBicicleta("66666666", "E_U2").getResultado());
        assertEquals(Retorno.Resultado.OK,
                s.devolverBicicleta("66666666", "E_U2").getResultado());

        // ---------- Ver ranking ----------
        retorno = s.rankingTiposPorUso();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        String[] partes = partesRanking(retorno.getValorString());
        assertEquals(3, partes.length);

        assertTipoYUsos(partes[0], "Electrica", 2);
        assertTipoYUsos(partes[1], "Mountain", 2);
        assertTipoYUsos(partes[2], "Urbana", 1);
    }
}
