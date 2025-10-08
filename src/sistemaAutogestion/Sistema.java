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
    private  ListaSE<Estacion> estaciones;

    // Bicicletas en DEPÓSITO en orden de ingreso (usar Adicionar al final)
    private  ListaSE<Bicicleta> deposito;

    // “Pila” de retiros para Deshacer últimos retiros (LIFO)
    private ListaSE<Alquiler> retiros;
    
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
        if (ci == null) return false;
        ci = ci.trim();
        if (ci.length() != 8) return false;
        for (int i = 0; i < 8; i++) {
            char c = ci.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }
    private boolean codigo6(String cod) {
        if (cod == null) return false;
    String c = cod.trim().toUpperCase();
    // exactamente 6 caracteres, solo A-Z y 0-9 (ASCII)
    return c.matches("[A-Z0-9]{6}");
}
    private Bicicleta.Tipo parseTipo(String tipoStr) {
        if (tipoStr == null) return null;
        String t = tipoStr.trim().toUpperCase(); 
        if (t.equals("URBANA"))   return Bicicleta.Tipo.URBANA;
        if (t.equals("MOUNTAIN")) return Bicicleta.Tipo.MOUNTAIN;
        if (t.equals("ELECTRICA"))return Bicicleta.Tipo.ELECTRICA;
        return null;
    }
    
    private Estacion buscarEstacion(String nombre) {
    nombre = sacaEspaciosMinus(nombre);
    Estacion est = null;
    int n = estaciones.Longitud();
    for (int i = 0; i < n; i++) {
        est = estaciones.Obtener(i);
        if (est.getNombre().toLowerCase().equals(nombre.toLowerCase())) return est;
    }
    return null;
    }

    
    private Usuario buscarUsuarioPorCI(String ci) {
        ci = sacaEspacios(ci);
        int n = usuarios.Longitud();
        Usuario u = null;
        for (int i = 0; i < n; i++) {
            u = usuarios.Obtener(i);
            if (u.getCedula().equals(ci)) return u; 
        }
        return null;
    }
    
    private Bicicleta buscarBicicletaPorCodigo(String cod) {
    cod = sacaEspacios(cod).toUpperCase();
    int n = bicicletas.Longitud();
    for (int i = 0; i < n; i++) {
        Bicicleta b = bicicletas.Obtener(i);
        if (b != null && cod.equals(b.getCodigo())) return b;
    }
    return null;
    }
    
    private Barrio barrioBuscar(String nombre) {
    nombre = sacaEspacios(nombre);
    int n = barrios.Longitud();
    for (int i = 0; i < n; i++) {
        Barrio b = barrios.Obtener(i);
        if (b.getNombre().equals(nombre)) return b;
    }
    return null;
}
    private Barrio barrioObtenerOCrear(String nombre) {
        nombre = sacaEspacios(nombre);
    Barrio b = barrioBuscar(nombre);
    if (b != null) return b;
    Barrio nuevo = new Barrio(nombre);
    barrios.adicionarOrdenado(nuevo); 
    return nuevo;
}

    // Llamalo cuando un alquiler se COMPLETA (en devolverBicicleta)
    private void incrementarUso(Bicicleta.Tipo tipo) {
    if (tipo == Bicicleta.Tipo.ELECTRICA)      usosElectrica++;
    else if (tipo == Bicicleta.Tipo.MOUNTAIN)  usosMountain++;
    else if (tipo == Bicicleta.Tipo.URBANA)    usosUrbana++;
}

    
    private void llevarBiciAlFinalDelDeposito(String codigo) {
    int n = deposito.Longitud();
    for (int i = 0; i < n; i++) {
        Bicicleta b = deposito.Obtener(i);
        if (b.getCodigo().equalsIgnoreCase(codigo)) {
            deposito.Eliminar(i);   // asumimos índice válido
            deposito.Adicionar(b);  // la pone al final
            return;
        }
    }
}
    
    
    @Override
    public Retorno crearSistemaDeGestion() {
        
    usuarios = new ListaSE<>();
    estaciones = new ListaSE<>();
    deposito = new ListaSE<>();
    retiros = new ListaSE<>();
    bicicletas = new ListaSE<>();
    barrios = new ListaSE<>(); 
    
    usosUrbana = usosMountain = usosElectrica = 0;

    
        return Retorno.ok();
    }

    @Override
    public Retorno registrarEstacion(String nombre, String barrio, int capacidad) {
        nombre = sacaEspacios(nombre);
        barrio = sacaEspacios(barrio);
        if (esVacio(nombre) || esVacio(barrio)) return Retorno.error1();
        if (capacidad <=0) return Retorno.error2();
        if (buscarEstacion(nombre) != null) return Retorno.error3();
        Barrio b = barrioObtenerOCrear(barrio);
        Estacion estacion = new Estacion(nombre, b, capacidad);
        estaciones.adicionarOrdenado(estacion);
        b.sumarCapacidad(capacidad);
        return Retorno.ok();
    }

    @Override
    public Retorno registrarUsuario(String cedula, String nombre) {
        cedula = sacaEspacios(cedula);
        nombre = sacaEspacios(nombre);
        if (esVacio(cedula) || esVacio(nombre)) return Retorno.error1();
        if (!ci8Digitos(cedula)) return Retorno.error2();
        if (buscarUsuarioPorCI(cedula) != null) return Retorno.error3();
        
        Usuario usuario = new Usuario(cedula, nombre);
        usuarios.adicionarOrdenado(usuario);
        
        return Retorno.ok();
    }

    @Override
    public Retorno registrarBicicleta(String codigo, String tipo) {
        codigo = sacaEspacios(codigo).toUpperCase();
        tipo = sacaEspacios(tipo);
        if (esVacio(codigo) || esVacio(tipo)) return Retorno.error1();
        if (!codigo6(codigo)) return Retorno.error2();
        if (parseTipo(tipo) == null) return Retorno.error3();
        if (buscarBicicletaPorCodigo(codigo) != null) return Retorno.error4();
        
        Bicicleta bici = new Bicicleta(codigo, parseTipo(tipo));
        deposito.adicionarOrdenado(bici);
        bicicletas.adicionarOrdenado(bici);
        
        return Retorno.ok();
    }

    @Override
    public Retorno marcarEnMantenimiento(String codigo, String motivo) {
        codigo = sacaEspacios(codigo);
        motivo = sacaEspacios(motivo);             
        if (esVacio(codigo) || esVacio(motivo)) return Retorno.error1();
        Bicicleta b = buscarBicicletaPorCodigo(codigo);
        if (b == null) return Retorno.error2();
        if (b.getEstado() == Bicicleta.Estado.ALQUILADA) return Retorno.error3();
        if (b.getEstado() == Bicicleta.Estado.MANTENIMIENTO) return Retorno.error4();
        
        Estacion ea = b.getEstacionActual();
        
        if (ea != null) {
            ea.retirarBiciPorCodigo(codigo);
            ea.getBarrio().restarAnclada();
            deposito.Adicionar(b);
        }
        b.setEstado(Bicicleta.Estado.MANTENIMIENTO); 
        b.setMotivoMantenimiento(motivo);
         
        return Retorno.ok();
    }

    @Override
    public Retorno repararBicicleta(String codigo) {
        codigo = sacaEspacios(codigo);
        if (esVacio(codigo)) return Retorno.error1();        
        Bicicleta b = buscarBicicletaPorCodigo(codigo);
        if (b == null) return Retorno.error2();
        if (b.getEstado() != Bicicleta.Estado.MANTENIMIENTO) return Retorno.error3();
        
        b.setEstado(Bicicleta.Estado.DISPONIBLE);
               
        return Retorno.ok();
    }

    @Override
    public Retorno eliminarEstacion(String nombre) {
        nombre = sacaEspacios(nombre);
        if (esVacio(nombre)) return Retorno.error1();
        Estacion estacionAEliminar = buscarEstacion(nombre);
        if (estacionAEliminar == null) return Retorno.error2();
        if (!estacionAEliminar.sinPendientes()) return Retorno.error3();
        
        //restarle capacidad al barrio
        estacionAEliminar.getBarrio().restarCapacidad(estacionAEliminar.getCapacidad());
        // 1. Encontramos el índice (la posición) de la estación que queremos eliminar.
        int indiceDeLaEstacion = this.estaciones.indiceDe(estacionAEliminar);

        // 2. Si el índice es válido (es decir, no es -1), eliminamos la estación de esa posición.
        if (indiceDeLaEstacion != -1) this.estaciones.Eliminar(indiceDeLaEstacion);
        
        return Retorno.ok();
    }
        

    @Override
    public Retorno asignarBicicletaAEstacion(String codigo, String nombreEstacion) {
        codigo = sacaEspacios(codigo);
        nombreEstacion = sacaEspacios(nombreEstacion);
        if (esVacio(codigo) || esVacio(nombreEstacion)) return Retorno.error1();
        Bicicleta b = buscarBicicletaPorCodigo(codigo);
        Estacion e = buscarEstacion(nombreEstacion);
        if (b == null || b.getEstado() != Bicicleta.Estado.DISPONIBLE) return Retorno.error2();
        if (e == null) return Retorno.error3();
        if (!e.tieneAnclajeLibre()) return Retorno.error4();
        
        Estacion estacionOrigen = b.getEstacionActual();
        
        if (estacionOrigen != null){
        estacionOrigen.retirarBiciPorCodigo(codigo);
        estacionOrigen.getBarrio().restarAnclada();
        e.anclarBicicleta(b);
        }else{
            int indice = deposito.indiceDe(b);
            deposito.Eliminar(indice);
            e.anclarBicicleta(b);
        }
        e.getBarrio().sumarAnclada();
        return Retorno.ok();
    }

    @Override
    public Retorno alquilarBicicleta(String cedula, String nombreEstacion) {
        


        return Retorno.noImplementada();
    }

    @Override
    public Retorno devolverBicicleta(String cedula, String nombreEstacionDestino) {
        
        
        //despues de devolver
        //incrementarUso(b.getTipo());

        
        return Retorno.noImplementada();
    }

    @Override
    public Retorno deshacerUltimosRetiros(int n) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno obtenerUsuario(String ci) {
    ci = sacaEspacios(ci);
    if(esVacio(ci)) return Retorno.error1();
    if(!ci8Digitos(ci)) return Retorno.error2();
    Usuario u = buscarUsuarioPorCI(ci);
    if (u == null) return Retorno.error3();
    return Retorno.ok(u.toString());
}

    @Override
    public Retorno listarUsuarios() {
        int total = usuarios.Longitud();
        String listado = "";
        for(int i = 0; i < total; i++){
            if (listado.length() != 0) listado += "|";
            listado += usuarios.Obtener(i).toString();
        }
        return Retorno.ok(listado);
    }

    @Override
    public Retorno listarBicisEnDeposito() {
        
    if (deposito == null || deposito.Vacia()) return Retorno.ok("");

    String salida = "";
    int n = deposito.Longitud();

    for (int i = 0; i < n; i++) {
        Bicicleta b = deposito.Obtener(i);

        if (!salida.isEmpty()) salida += "|";

        String estado = (b.getEstado() == Bicicleta.Estado.MANTENIMIENTO) ? "Mantenimiento" : "Disponible";

        salida += b.getCodigo().toUpperCase()
             + "#" + b.getTipo().name()
             + "#" + estado;
    }

    return Retorno.ok(salida);

    }
    
    
    // Helper: considera estación a cualquier celda que empiece con 'E' (ej: "E1", "E3", etc.)
    private boolean esEstacion(String cell) {
    if (cell == null) return false;
    String s = cell.trim();
    if (s.isEmpty()) return false;
    char c0 = s.charAt(0);
    return (c0 == 'E' || c0 == 'e');
    
}
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
        if (filaCount > maxFila) maxFila = filaCount;
    }

    // Máximo por columna
    int maxCol = 0;
    for (int j = 0; j < columnas; j++) {
        if (colCount[j] > maxCol) maxCol = colCount[j];
    }

    // Parte 1: "max#fila|columna|ambas"
    String tipo;
    if (maxFila > maxCol)      tipo = "fila";
    else if (maxCol > maxFila) tipo = "columna";
    else                       tipo = "ambas";

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
    if (maxGlobal == 0) parte1 = "0#ambas";

    return Retorno.ok(parte1 + "|" + parte2);
    }

    @Override
    public Retorno listarBicicletasDeEstacion(String nombreEstacion) {
        nombreEstacion = sacaEspacios(nombreEstacion);
        if (esVacio(nombreEstacion)) return Retorno.error1();//no se pide pero por si las dudas
        Estacion e = buscarEstacion(nombreEstacion);
        if (e == null) return Retorno.error2();
        String listado = e.listarCodigosOrdenados();
        return Retorno.ok(listado);
       
    }

    @Override
    public Retorno estacionesConDisponibilidad(int n) {
        if (n <= 1) return Retorno.error1();//no deberia ser <1??
        int ce = estaciones.Longitud();//ce = cantidad de estaciones
        int contador = 0;
        for (int i = 0; i < ce; i++){
            Estacion e = estaciones.Obtener(i);
            int bicicletasDisponibles = e.getOcupacion();
            if (bicicletasDisponibles > n) contador++;
        }
        return Retorno.ok(contador);
        
    }

    @Override
    public Retorno ocupacionPromedioXBarrio() {
        if (barrios.Vacia()) return Retorno.ok("");
        String salida = "";
        int cantB = barrios.Longitud();
        for (int i = 0; i < cantB; i++){
            Barrio b = barrios.Obtener(i);
            int capTot = b.getCapacidadTotal();
            if (capTot > 0) {
                int ancladas = b.getAncladas();
                int promedio = (ancladas * 100 + capTot / 2) / capTot;  // redondeo entero
                if (!salida.isEmpty()) salida += "|";
                salida += b.getNombre() + "#" + promedio;
            }
        }
        return Retorno.ok(salida);
    }

    @Override
    public Retorno rankingTiposPorUso() {
         // Parejas iniciales (tipo, usos) a partir de los contadores actuales
    String tipoPrimero   = "Electrica"; int usosPrimero   = usosElectrica;
    String tipoSegundo   = "Mountain";  int usosSegundo   = usosMountain;
    String tipoTercero   = "Urbana";    int usosTercero   = usosUrbana;

    // Queremos: más usos primero; si empatan, alfabético por tipo

    // Aseguro que (tipoPrimero, usosPrimero) sea mejor que (tipoSegundo, usosSegundo)
    if (usosSegundo > usosPrimero ||
       (usosSegundo == usosPrimero && tipoSegundo.compareToIgnoreCase(tipoPrimero) < 0)) {

        String tipoTemporal = tipoPrimero;  tipoPrimero = tipoSegundo;  tipoSegundo = tipoTemporal;
        int    usosTemporal = usosPrimero;  usosPrimero = usosSegundo;  usosSegundo = usosTemporal;
    }

    // Comparo el tercero con el NUEVO primero para que el primero sea el mejor de los tres
    if (usosTercero > usosPrimero ||
       (usosTercero == usosPrimero && tipoTercero.compareToIgnoreCase(tipoPrimero) < 0)) {

        String tipoTemporal = tipoPrimero;  tipoPrimero = tipoTercero;  tipoTercero = tipoTemporal;
        int    usosTemporal = usosPrimero;  usosPrimero = usosTercero;  usosTercero = usosTemporal;
    }

    // Ordeno segundo y tercero entre sí (segundo debe quedar >= tercero)
    if (usosTercero > usosSegundo ||
       (usosTercero == usosSegundo && tipoTercero.compareToIgnoreCase(tipoSegundo) < 0)) {

        String tipoTemporal = tipoSegundo;  tipoSegundo = tipoTercero;  tipoTercero = tipoTemporal;
        int    usosTemporal = usosSegundo;  usosSegundo = usosTercero;  usosTercero = usosTemporal;
    }

    String salida =
        tipoPrimero + "#" + usosPrimero + "|" +
        tipoSegundo + "#" + usosSegundo + "|" +
        tipoTercero + "#" + usosTercero;

    return Retorno.ok(salida);
    }

    @Override
    public Retorno usuariosEnEspera(String nombreEstacion) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno usuarioMayor() {
        return Retorno.noImplementada();
    }

}
