package sistemaAutogestion;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test3_04InformacionMapa {

    private Retorno retorno;
    private final IObligatorio s = new Sistema();

    @Before
    public void setUp() {
        s.crearSistemaDeGestion();
    }

    @Test
    public void informacionMapaNull() {
        // mapa null → "0#ambas|no existe"
        retorno = s.informaciónMapa(null);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("0#ambas|no existe", retorno.getValorString());
    }

    @Test
    public void informacionMapaVacio() {
        // mapa sin filas o sin columnas → "0#ambas|no existe"
        String[][] mapa1 = new String[0][0];
        retorno = s.informaciónMapa(mapa1);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("0#ambas|no existe", retorno.getValorString());

        String[][] mapa2 = new String[1][0];
        retorno = s.informaciónMapa(mapa2);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("0#ambas|no existe", retorno.getValorString());
    }

    @Test
    public void informacionMapaSinEstaciones() {
        // Mapa con celdas pero ninguna estación → "0#ambas|no existe"
        String[][] mapa = {
                {"", null, "x"},
                {"-", " ", "0"}
        };
        retorno = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("0#ambas|no existe", retorno.getValorString());
    }

    @Test
    public void informacionMapaFilaConMasEstaciones() {
        // Fila 0 tiene 2 estaciones, columnas máximo 1 → "2#fila|no existe"
        String[][] mapa = {
                {"E1", "E2", ""},
                {"", "", ""}
        };
        retorno = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("2#fila|no existe", retorno.getValorString());
    }

    @Test
    public void informacionMapaColumnaConMasEstaciones() {
        // Columna 0 tiene 3, fila máxima es 2 → "3#columna|no existe"
        String[][] mapa = {
                {"E1", ""},
                {"E2", "E3"},
                {"E4", ""}
        };
        retorno = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("3#columna|no existe", retorno.getValorString());
    }

    @Test
    public void informacionMapaEmpateFilaColumnaAmbas() {
        // 2x2, una estación por fila y por columna → "1#ambas|no existe"
        String[][] mapa = {
                {"E1", ""},
                {"", "e2"}  // también chequea minúscula (e)
        };
        retorno = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("1#ambas|no existe", retorno.getValorString());
    }

    @Test
    public void informacionMapaConTresColumnasAscendentes() {
        // colCount: [0,1,2,0] → existe 0<1<2 en columnas 0-1-2
        // filas: [2,1,0] → maxFila=2; columnas maxCol=2 → "2#ambas|existe"
        String[][] mapa = {
                {"",  "E1", "E2", ""},  // fila 0: 2 estaciones
                {"",  "",   "E3", ""},  // fila 1: 1 estación
                {"",  "",   "",   ""}   // fila 2: 0 estaciones
        };

        retorno = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("2#ambas|existe", retorno.getValorString());
    }

    @Test
    public void informacionMapaSinTresColumnasAscendentes() {
        // colCount: [1,1,0] → nunca estrictamente ascendente → "2#fila|no existe"
        String[][] mapa = {
                {"E1", "E2", ""},
                {"",   "",   ""}
        };

        retorno = s.informaciónMapa(mapa);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("2#fila|no existe", retorno.getValorString());
    }
}
