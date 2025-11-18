package sistemaAutogestion;

//Santiago Neira No. 334109
//Cesar Martinez No. 330903
import dominio.*;
import tads.*;

public class Sistema implements IObligatorio {

    // =======================
    // Listas
    // =======================
    // Usuarios ordenados por NOMBRE (Usuario implements Comparable<Usuario>)
    private ListaSE<Usuario> usuarios;

    // Estaciones ordenadas por NOMBRE (si querés listarlas por nombre)
    private ListaSE<Estacion> estaciones;

    // Bicicletas en DEPÓSITO en orden de ingreso (usar Adicionar al final)
    private ListaSE<Bicicleta> deposito;

    // “Pila” de retiros para Deshacer últimos retiros (LIFO)
    private ListaSE<Alquiler> alquileres;

    private PilaSE<Alquiler> historicoAlquileres;

    private ListaSE<Bicicleta> bicicletas;

    private ListaSE<Barrio> barrios;

    // =======================
    // Contadores/estadísticas
    // =======================
    // Ranking por uso (incrementar al FINALIZAR un alquiler)
    private int usosUrbana = 0;
    private int usosMountain = 0;
    private int usosElectrica = 0;

    // =======================
    // Helpers Varios
    // =======================
    //saca espacios
    private String sacaEspacios(String s) {
        return s == null ? null : s.trim();
    }

    private String sacaEspaciosMinus(String s) {     // recorta + lowercase (para comparar nombres/barrio)
        return s == null ? null : s.trim().toLowerCase();
    }

    private boolean esVacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    private boolean ci8Digitos(String ci) {
        if (ci == null) {
            return false;
        }
        ci = ci.trim();
        if (ci.length() != 8) {
            return false;
        }
        for (int i = 0; i < 8; i++) {
            char c = ci.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private boolean codigo6(String cod) {
        if (cod == null) {
            return false;
        }
        String c = cod.trim().toUpperCase();
        // exactamente 6 caracteres, solo A-Z y 0-9 (ASCII)
        return c.matches("[A-Z0-9]{6}");
    }

    private Bicicleta.Tipo parseTipo(String tipoStr) {
        if (tipoStr == null) {
            return null;
        }
        String t = tipoStr.trim().toUpperCase();
        if (t.equals("URBANA")) {
            return Bicicleta.Tipo.URBANA;
        }
        if (t.equals("MOUNTAIN")) {
            return Bicicleta.Tipo.MOUNTAIN;
        }
        if (t.equals("ELECTRICA")) {
            return Bicicleta.Tipo.ELECTRICA;
        }
        return null;
    }

    private void ocuparAnclajeLibre(Estacion e) {
        String codigoBici = e.existenBicicletasParaAnclar();
        if (codigoBici != null && !codigoBici.isEmpty()) {
            Bicicleta b = buscarBicicletaPorCodigo(codigoBici);
            Alquiler a = buscarAlquilerPorCodigoBici(codigoBici);
            if (a != null) {
                String cedula = a.getCedulaUsuario();
                Usuario u = buscarUsuarioPorCI(cedula);
                u.setCodigoBiciActual(null);
                a.finalizar(e.getNombre());
            }
            Estacion estacionOrigen = b.getEstacionActual();
            estacionOrigen.retirarBiciPorCodigo(codigoBici);
            estacionOrigen.getBarrio().restarAnclada();
            e.anclarBicicleta(b);
            e.getBarrio().sumarAnclada();
        }

    }

    private Estacion buscarEstacion(String nombre) {
        nombre = sacaEspaciosMinus(nombre);
        Estacion est = null;
        int n = estaciones.Longitud();
        for (int i = 0; i < n; i++) {
            est = estaciones.Obtener(i);
            if (est.getNombre().toLowerCase().equals(nombre.toLowerCase())) {
                return est;
            }
        }
        return null;
    }

    private Alquiler buscarAlquilerPorCodigoBici(String codigoBici) {
        if (codigoBici == null) {
            return null;
        }

        codigoBici = sacaEspaciosMinus(codigoBici);
        int n = alquileres.Longitud();

        for (int i = 0; i < n; i++) {
            Alquiler a = alquileres.Obtener(i);
            if (a != null && a.getCodigoBici() != null
                    && a.getCodigoBici().equalsIgnoreCase(codigoBici) && a.esActivo()) {
                return a;
            }
        }
        return null;
    }

    private Usuario buscarUsuarioPorCI(String ci) {
        ci = sacaEspacios(ci);
        int n = usuarios.Longitud();
        Usuario u = null;
        for (int i = 0; i < n; i++) {
            u = usuarios.Obtener(i);
            if (u.getCedula().equals(ci)) {
                return u;
            }
        }
        return null;
    }

    private Bicicleta buscarBicicletaPorCodigo(String cod) {
        cod = sacaEspacios(cod).toUpperCase();
        int n = bicicletas.Longitud();
        for (int i = 0; i < n; i++) {
            Bicicleta b = bicicletas.Obtener(i);
            if (b != null && cod.equals(b.getCodigo())) {
                return b;
            }
        }
        return null;
    }

    private Barrio barrioBuscar(String nombre) {
        nombre = sacaEspacios(nombre);
        int n = barrios.Longitud();
        for (int i = 0; i < n; i++) {
            Barrio b = barrios.Obtener(i);
            if (b.getNombre().equals(nombre)) {
                return b;
            }
        }
        return null;
    }

    private Barrio barrioObtenerOCrear(String nombre) {
        nombre = sacaEspacios(nombre);
        Barrio b = barrioBuscar(nombre);
        if (b != null) {
            return b;
        }
        Barrio nuevo = new Barrio(nombre);
        barrios.adicionarOrdenado(nuevo);
        return nuevo;
    }

    // Lo llamamos cuando un alquiler se INICIA (en alquilarBicicleta)
    private void incrementarUso(Bicicleta.Tipo tipo) {
        if (tipo == Bicicleta.Tipo.ELECTRICA) {
            usosElectrica++;
        } else if (tipo == Bicicleta.Tipo.MOUNTAIN) {
            usosMountain++;
        } else if (tipo == Bicicleta.Tipo.URBANA) {
            usosUrbana++;
        }
    }

    //2.1
    @Override
    public Retorno crearSistemaDeGestion() {

        usuarios = new ListaSE<>();
        estaciones = new ListaSE<>();
        deposito = new ListaSE<>();
        alquileres = new ListaSE<>();
        historicoAlquileres = new PilaSE<>();
        bicicletas = new ListaSE<>();
        barrios = new ListaSE<>();

        usosUrbana = usosMountain = usosElectrica = 0;

        return Retorno.ok();
    }

    //2.2
    @Override
    public Retorno registrarEstacion(String nombre, String barrio, int capacidad) {
        nombre = sacaEspacios(nombre);
        barrio = sacaEspacios(barrio);
        if (esVacio(nombre) || esVacio(barrio)) {
            return Retorno.error1();
        }
        if (capacidad <= 0) {
            return Retorno.error2();
        }
        if (buscarEstacion(nombre) != null) {
            return Retorno.error3();
        }
        Barrio b = barrioObtenerOCrear(barrio);
        Estacion estacion = new Estacion(nombre, b, capacidad);
        estaciones.adicionarOrdenado(estacion);
        b.sumarCapacidad(capacidad);
        return Retorno.ok();
    }

    //2.3
    @Override
    public Retorno registrarUsuario(String cedula, String nombre) {
        cedula = sacaEspacios(cedula);
        nombre = sacaEspacios(nombre);
        if (esVacio(cedula) || esVacio(nombre)) {
            return Retorno.error1();
        }
        if (!ci8Digitos(cedula)) {
            return Retorno.error2();
        }
        if (buscarUsuarioPorCI(cedula) != null) {
            return Retorno.error3();
        }

        Usuario usuario = new Usuario(cedula, nombre);
        usuarios.adicionarOrdenado(usuario);

        return Retorno.ok();
    }

    //2.4
    @Override
    public Retorno registrarBicicleta(String codigo, String tipo) {
        codigo = sacaEspacios(codigo);
        tipo = sacaEspacios(tipo);
        if (esVacio(codigo) || esVacio(tipo)) {
            return Retorno.error1();
        }
        if (!codigo6(codigo)) {
            return Retorno.error2();
        }
        if (parseTipo(tipo) == null) {
            return Retorno.error3();
        }
        if (buscarBicicletaPorCodigo(codigo) != null) {
            return Retorno.error4();
        }

        Bicicleta bici = new Bicicleta(codigo, parseTipo(tipo));
        deposito.Adicionar(bici);
        bicicletas.adicionarOrdenado(bici);

        return Retorno.ok();
    }

    //2.5
    @Override
    public Retorno marcarEnMantenimiento(String codigo, String motivo) {
        codigo = sacaEspacios(codigo);
        motivo = sacaEspacios(motivo);
        if (esVacio(codigo) || esVacio(motivo)) {
            return Retorno.error1();
        }
        Bicicleta b = buscarBicicletaPorCodigo(codigo);
        if (b == null) {
            return Retorno.error2();
        }
        if (b.getEstado() == Bicicleta.Estado.ALQUILADA) {
            return Retorno.error3();
        }
        if (b.getEstado() == Bicicleta.Estado.MANTENIMIENTO) {
            return Retorno.error4();
        }

        Estacion ea = b.getEstacionActual();

        if (ea != null) {
            ea.retirarBiciPorCodigo(codigo);
            ea.getBarrio().restarAnclada();
            deposito.Adicionar(b);
            ocuparAnclajeLibre(ea);
        }
        b.setEstado(Bicicleta.Estado.MANTENIMIENTO);
        b.setMotivoMantenimiento(motivo);

        return Retorno.ok();
    }

    //2.6
    @Override
    public Retorno repararBicicleta(String codigo) {
        codigo = sacaEspacios(codigo);
        if (esVacio(codigo)) {
            return Retorno.error1();
        }
        Bicicleta b = buscarBicicletaPorCodigo(codigo);
        if (b == null) {
            return Retorno.error2();
        }
        if (b.getEstado() != Bicicleta.Estado.MANTENIMIENTO) {
            return Retorno.error3();
        }

        b.setEstado(Bicicleta.Estado.DISPONIBLE);

        return Retorno.ok();
    }

    //2.7
    @Override
    public Retorno eliminarEstacion(String nombre) {
        nombre = sacaEspacios(nombre);
        if (esVacio(nombre)) {
            return Retorno.error1();
        }
        Estacion estacionAEliminar = buscarEstacion(nombre);
        if (estacionAEliminar == null) {
            return Retorno.error2();
        }
        if (!estacionAEliminar.sinPendientes()) {
            return Retorno.error3();
        }

        //restarle capacidad al barrio
        estacionAEliminar.getBarrio().restarCapacidad(estacionAEliminar.getCapacidad());
        // 1. Encontramos el índice (la posición) de la estación que queremos eliminar.
        int indiceDeLaEstacion = this.estaciones.indiceDe(estacionAEliminar);

        // 2. Si el índice es válido (es decir, no es -1), eliminamos la estación de esa posición.
        if (indiceDeLaEstacion != -1) {
            this.estaciones.Eliminar(indiceDeLaEstacion);
        }

        return Retorno.ok();
    }

    //2.8
    @Override
    public Retorno asignarBicicletaAEstacion(String codigo, String nombreEstacion) {
        codigo = sacaEspacios(codigo);
        nombreEstacion = sacaEspacios(nombreEstacion);
        if (esVacio(codigo) || esVacio(nombreEstacion)) {
            return Retorno.error1();
        }
        Bicicleta b = buscarBicicletaPorCodigo(codigo);
        Estacion e = buscarEstacion(nombreEstacion);
        if (b == null || b.getEstado() != Bicicleta.Estado.DISPONIBLE) {
            return Retorno.error2();
        }
        if (e == null) {
            return Retorno.error3();
        }
        if (!e.tieneAnclajeLibre()) {
            return Retorno.error4();
        }

        Estacion estacionOrigen = b.getEstacionActual();

        if (estacionOrigen != null) {
            estacionOrigen.retirarBiciPorCodigo(codigo);
            estacionOrigen.getBarrio().restarAnclada();
            e.anclarBicicleta(b);

        } else {
            int indice = deposito.indiceDe(b);
            deposito.Eliminar(indice);
            e.anclarBicicleta(b);

        }
        e.getBarrio().sumarAnclada();
        return Retorno.ok();
    }

    //2.9
    @Override
    public Retorno alquilarBicicleta(String cedula, String nombreEstacion) {
        if (esVacio(cedula) || esVacio(nombreEstacion)) {
            return Retorno.error1();
        }
        Usuario u = buscarUsuarioPorCI(cedula);
        if (u == null) {
            return Retorno.error2();
        }
        Estacion e = buscarEstacion(nombreEstacion);
        if (e == null) {
            return Retorno.error3();
        }
        if (u.getCodigoBiciActual() != null) {
            return Retorno.error4();//error si el usuario tiene bici alquilada
        }

        Bicicleta b = e.retirarDisponibleParaAlquiler();

        if (b == null) {
            e.colaAlquiler.encolar(cedula);
        } else {
            String codigo = b.getCodigo();
            u.setCodigoBiciActual(codigo);
            b.setEstado(Bicicleta.Estado.ALQUILADA);
            Alquiler a = new Alquiler(codigo, cedula, e.getNombre());
            alquileres.Adicionar(a);
            historicoAlquileres.apilar(a);
            incrementarUso(b.getTipo());
            //aca queda anclaje libre... entonces hay que traer bici si es que la hay en el anclaje de espera
            ocuparAnclajeLibre(e);
        }

        return Retorno.ok();
    }

    //2.10
    @Override
    public Retorno devolverBicicleta(String cedula, String nombreEstacionDestino) {
        if (esVacio(cedula) || esVacio(nombreEstacionDestino)) {
            return Retorno.error1();
        }
        Usuario u = buscarUsuarioPorCI(cedula);

        Estacion ed = buscarEstacion(nombreEstacionDestino);
        if (u == null || !u.tieneAlquilerActivo()) {
            return Retorno.error2();
        }
        Bicicleta b = buscarBicicletaPorCodigo(u.getCodigoBiciActual());
        Alquiler a = buscarAlquilerPorCodigoBici(b.getCodigo());

        if (ed == null) {
            return Retorno.error3();
        }

        if (!ed.tieneAnclajeLibre()) {
            ed.colaAnclaje.encolar(u.getCodigoBiciActual());
            a.marcarEsperaAnclaje(nombreEstacionDestino);//actualiza el alquiler
        } else {
            ed.anclarBicicleta(b);
            b.setEstadoDisponible();
            u.setCodigoBiciActual(null);

            a.finalizar(nombreEstacionDestino);
            u.sumarAlquileresCompletados();

            String posibleUsuarioDestino = ed.desencolarEsperaAlquiler();
            if (posibleUsuarioDestino != null) {
                alquilarBicicleta(posibleUsuarioDestino, nombreEstacionDestino);
            }
        }

        return Retorno.ok();
    }

    //2.11
    @Override
    public Retorno deshacerUltimosRetiros(int n) {
        if (n <= 0) {
            return Retorno.error1();
        }
        int contadorDeDesapilado = n;
        String listado = "";
        while (contadorDeDesapilado > 0 && !historicoAlquileres.estaVacia()) {
            Alquiler a = historicoAlquileres.desapilar();
            Bicicleta b = buscarBicicletaPorCodigo(a.getCodigoBici());
            Usuario u = buscarUsuarioPorCI(a.getCedulaUsuario());
            Estacion estOrigen = buscarEstacion(a.getEstacionOrigen());

            //le saco la bicileta al usuario
            u.setCodigoBiciActual(null);
            if (estOrigen.tieneAnclajeLibre()) {
                estOrigen.anclarBicicleta(b);
                b.setEstadoDisponible();
            } else {
                estOrigen.colaAnclaje.encolar(b.getCodigo());

            }
            if (b.getTipo() == Bicicleta.Tipo.ELECTRICA) {
                usosElectrica--;
            }
            if (b.getTipo() == Bicicleta.Tipo.MOUNTAIN) {
                usosMountain--;
            }
            if (b.getTipo() == Bicicleta.Tipo.URBANA) {
                usosUrbana--;
            }

            a.finalizar(a.getEstacionOrigen());
            if (!listado.isEmpty()) {
                listado += "|";
            }
            listado += a.formatoDeshacer();
            contadorDeDesapilado--;
        }

        return Retorno.ok(listado);
    }

    //3.1
    @Override
    public Retorno obtenerUsuario(String ci) {
        ci = sacaEspacios(ci);
        if (esVacio(ci)) {
            return Retorno.error1();
        }
        if (!ci8Digitos(ci)) {
            return Retorno.error2();
        }
        Usuario u = buscarUsuarioPorCI(ci);
        if (u == null) {
            return Retorno.error3();
        }
        return Retorno.ok(u.toString());
    }

    //3.2
    @Override
    public Retorno listarUsuarios() {
        int total = usuarios.Longitud();
        String listado = "";
        for (int i = 0; i < total; i++) {
            if (listado.length() != 0) {
                listado += "|";
            }
            listado += usuarios.Obtener(i).toString();
        }
        return Retorno.ok(listado);
    }

    //3.3
    @Override //Recursiva
    public Retorno listarBicisEnDeposito() {

        if (deposito == null || deposito.Vacia()) {
            return Retorno.ok("");
        }
        String salida = listarAux(0);
        return Retorno.ok(salida);
    }

    private String listarAux(int n) {

        int x = deposito.Longitud();

        // Caso base: llegamos al final de la lista
        if (n == x) {
            return "";
        }

        // Tomo la bici en posición n
        Bicicleta b = deposito.Obtener(n);

        String estado = (b.getEstado() == Bicicleta.Estado.MANTENIMIENTO)
                ? "Mantenimiento"
                : "Disponible";

        String actual = b.getCodigo().toUpperCase()
                + "#" + b.getTipo().name()
                + "#" + estado;

        // Llamada recursiva para el resto
        String resto = listarAux(n + 1);

        // Si no hay más bicis después, devuelvo solo la actual
        if (resto.isEmpty()) {
            return actual;
        }

        // Si hay más, concateno con "|"
        return actual + "|" + resto;
    }

    /*
    @Override //Iterativa
    public Retorno listarBicisEnDeposito() {

        if (deposito == null || deposito.Vacia()) {
            return Retorno.ok("");
        }

        String salida = "";
        int n = deposito.Longitud();

        for (int i = 0; i < n; i++) {
            Bicicleta b = deposito.Obtener(i);

            if (!salida.isEmpty()) {
                salida += "|";
            }

            String estado = (b.getEstado() == Bicicleta.Estado.MANTENIMIENTO) ? "Mantenimiento" : "Disponible";

            salida += b.getCodigo().toUpperCase()
                    + "#" + b.getTipo().name()
                    + "#" + estado;
        }

        return Retorno.ok(salida);

    }
     */
    // Helper: considera estación a cualquier celda que empiece con 'E' (ej: "E1", "E3", etc.)
    private boolean esEstacion(String cell) {
        if (cell == null) {
            return false;
        }
        String s = cell.trim();
        if (s.isEmpty()) {
            return false;
        }
        char c0 = s.charAt(0);
        return (c0 == 'E' || c0 == 'e');

    }

    //3.4
    @Override
    public Retorno informaciónMapa(String[][] mapa) {
        // Manejo de nulos / vacíos
        if (mapa == null || mapa.length == 0 || mapa[0] == null || mapa[0].length == 0) {
            return Retorno.ok("0#ambas|no existe");
        }

        final int filas = mapa.length;
        final int columnas = mapa[0].length;

        int maxFila = 0;
        int[] colCount = new int[columnas];

        // Contar estaciones por fila y acumular por columna en una sola pasada
        for (int i = 0; i < filas; i++) {
            int filaCount = 0;
            for (int j = 0; j < columnas; j++) {
                String cell = mapa[i][j];
                if (esEstacion(cell)) {
                    filaCount++;
                    colCount[j]++;
                }
            }
            if (filaCount > maxFila) {
                maxFila = filaCount;
            }
        }

        // Máximo por columna
        int maxCol = 0;
        for (int j = 0; j < columnas; j++) {
            if (colCount[j] > maxCol) {
                maxCol = colCount[j];
            }
        }

        // Parte 1: "max#fila|columna|ambas"
        String tipo;
        if (maxFila > maxCol) {
            tipo = "fila";
        } else if (maxCol > maxFila) {
            tipo = "columna";
        } else {
            tipo = "ambas";
        }

        int maxGlobal = Math.max(maxFila, maxCol);
        String parte1 = maxGlobal + "#" + tipo;

        // Parte 2: existencia de 3 columnas consecutivas con conteo estrictamente ascendente
        boolean existeAsc = false;
        if (columnas >= 3) {
            for (int j = 0; j <= columnas - 3; j++) {
                if (colCount[j] < colCount[j + 1] && colCount[j + 1] < colCount[j + 2]) {
                    existeAsc = true;
                    break;
                }
            }
        }
        String parte2 = existeAsc ? "existe" : "no existe";

        // Caso todo vacío: especificación pide "0#ambas"
        if (maxGlobal == 0) {
            parte1 = "0#ambas";
        }

        return Retorno.ok(parte1 + "|" + parte2);
    }

    //3.5
    @Override
    public Retorno listarBicicletasDeEstacion(String nombreEstacion) {
        nombreEstacion = sacaEspacios(nombreEstacion);
        if (esVacio(nombreEstacion)) {
            return Retorno.error1();//no se pide pero por si las dudas
        }
        Estacion e = buscarEstacion(nombreEstacion);
        if (e == null) {
            return Retorno.error2();
        }
        String listado = e.listarCodigosOrdenados();
        return Retorno.ok(listado);

    }

    //3.6
    @Override
    public Retorno estacionesConDisponibilidad(int n) {
        if (n <= 1) {
            return Retorno.error1();//no deberia ser <1??
        }
        int ce = estaciones.Longitud();//ce = cantidad de estaciones
        int contador = 0;
        for (int i = 0; i < ce; i++) {
            Estacion e = estaciones.Obtener(i);
            int bicicletasDisponibles = e.getOcupacion();
            if (bicicletasDisponibles > n) {
                contador++;
            }
        }
        return Retorno.ok(contador);

    }

    //3.7
    @Override
    public Retorno ocupacionPromedioXBarrio() {
        if (barrios.Vacia()) {
            return Retorno.ok("");
        }
        String salida = "";
        int cantB = barrios.Longitud();
        for (int i = 0; i < cantB; i++) {
            Barrio b = barrios.Obtener(i);
            int capTot = b.getCapacidadTotal();
            if (capTot > 0) {
                int ancladas = b.getAncladas();
                int promedio = (ancladas * 100 + capTot / 2) / capTot;  // redondeo entero
                if (!salida.isEmpty()) {
                    salida += "|";
                }
                salida += b.getNombre() + "#" + promedio;
            }
        }
        return Retorno.ok(salida);
    }

    //3.8
    @Override
    public Retorno rankingTiposPorUso() {
        // Parejas iniciales (tipo, usos) a partir de los contadores actuales
        String tipoPrimero = "ELECTRICA";
        int usosPrimero = usosElectrica;
        String tipoSegundo = "MOUNTAIN";
        int usosSegundo = usosMountain;
        String tipoTercero = "URBANA";
        int usosTercero = usosUrbana;

        // Queremos: más usos primero; si empatan, alfabético por tipo
        // Aseguro que (tipoPrimero, usosPrimero) sea mayor que (tipoSegundo, usosSegundo)
        if (usosSegundo > usosPrimero
                || (usosSegundo == usosPrimero && tipoSegundo.compareToIgnoreCase(tipoPrimero) < 0)) {

            String tipoTemporal = tipoPrimero;
            tipoPrimero = tipoSegundo;
            tipoSegundo = tipoTemporal;
            int usosTemporal = usosPrimero;
            usosPrimero = usosSegundo;
            usosSegundo = usosTemporal;
        }

        // Comparo el tercero con el NUEVO primero para que el primero sea el mejor de los tres
        if (usosTercero > usosPrimero
                || (usosTercero == usosPrimero && tipoTercero.compareToIgnoreCase(tipoPrimero) < 0)) {

            String tipoTemporal = tipoPrimero;
            tipoPrimero = tipoTercero;
            tipoTercero = tipoTemporal;
            int usosTemporal = usosPrimero;
            usosPrimero = usosTercero;
            usosTercero = usosTemporal;
        }

        // Ordeno segundo y tercero entre sí (segundo debe quedar >= tercero)
        if (usosTercero > usosSegundo
                || (usosTercero == usosSegundo && tipoTercero.compareToIgnoreCase(tipoSegundo) < 0)) {

            String tipoTemporal = tipoSegundo;
            tipoSegundo = tipoTercero;
            tipoTercero = tipoTemporal;
            int usosTemporal = usosSegundo;
            usosSegundo = usosTercero;
            usosTercero = usosTemporal;
        }

        String salida
                = tipoPrimero + "#" + usosPrimero + "|"
                + tipoSegundo + "#" + usosSegundo + "|"
                + tipoTercero + "#" + usosTercero;

        return Retorno.ok(salida);
    }

    //3.9
    @Override
    public Retorno usuariosEnEspera(String nombreEstacion) {
        // Validación 1: parámetro vacío o null
        if (esVacio(nombreEstacion)) {
            return Retorno.error1();
        }

        // Validación 2: estación no existe
        Estacion e = buscarEstacion(nombreEstacion);
        if (e == null) {
            return Retorno.error2();
        }

        String listado = "";
        ColaSE<String> aux = new ColaSE<>();
        ColaSE<String> cola = e.colaAlquiler;

        while (!cola.estaVacia()) {
            String cedula = cola.desencolar();
            if (!listado.isEmpty()) {
                listado += "|";
            }
            listado += cedula;
            aux.encolar(cedula);
        }
        while (!aux.estaVacia()) {
            cola.encolar(aux.desencolar());
        }
        return Retorno.ok(listado);
    }

    //3.10
    @Override
    public Retorno usuarioMayor() {
        String usuarioMayor = "";
        int cantidadAlquileresPorUsuario = -1;
        int cantidadTotalUsuarios = usuarios.Longitud();
        for (int i = 0; i < cantidadTotalUsuarios; i++) {
            Usuario u = usuarios.Obtener(i);
            if (u.getAlquileresCompletados() > cantidadAlquileresPorUsuario) {
                cantidadAlquileresPorUsuario = u.getAlquileresCompletados();
                usuarioMayor = u.getCedula();
            } else if (u.getAlquileresCompletados() == cantidadAlquileresPorUsuario) {
                if (!usuarioMayor.isEmpty() && u.getCedula().compareTo(usuarioMayor) < 0) {
                    usuarioMayor = u.getCedula();
                }
            }
        }
        return Retorno.ok(usuarioMayor);
    }

}
