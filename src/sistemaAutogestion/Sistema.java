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

    private boolean esVacio(String s) {
        return s == null || s.trim().isEmpty();
    }
    private boolean ci8Digitos(String ci) {
        if (ci == null || ci.length() != 8) return false;
        for (int i = 0; i < 8; i++) {
            char c = ci.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }
    private boolean codigo6(String cod) {
        return cod != null && cod.length() == 6;
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
    Estacion est = null;
    int n = estaciones.Longitud();
    for (int i = 0; i < n; i++) {
        est = estaciones.Obtener(i);
        if (est.getNombre().equals(nombre)) return est;
    }
    return null;
    }
    
    private Usuario buscarUsuarioPorCI(String ci) {
        int n = usuarios.Longitud();
        Usuario u = null;
        for (int i = 0; i < n; i++) {
            u = usuarios.Obtener(i);
            if (u.getCedula().equals(ci)) return u; 
        }
        return null;
    }
    
    private Bicicleta buscarBicicletaPorCodigo(String cod) {
    String key = cod.trim();
    int n = deposito.Longitud();
    for (int i = 0; i < n; i++) {
        Bicicleta b = deposito.Obtener(i);
        if (b != null && key.equals(b.getCodigo())) return b;
    }
    return null;
}
    

    @Override
    public Retorno crearSistemaDeGestion() {
        
    usuarios = new ListaSE<>();
    estaciones = new ListaSE<>();
    deposito = new ListaSE<>();
    retiros = new ListaSE<>();
    
        return Retorno.ok();
    }

    @Override
    public Retorno registrarEstacion(String nombre, String barrio, int capacidad) {
        if (esVacio(nombre) || esVacio(barrio)) return Retorno.error1();
        if (capacidad <=0) return Retorno.error2();
        if (buscarEstacion(nombre) != null) return Retorno.error3();
        
        Estacion estacion = new Estacion(nombre, barrio, capacidad);
        estaciones.adicionarOrdenado(estacion);
        
        return Retorno.ok();
    }

    @Override
    public Retorno registrarUsuario(String cedula, String nombre) {
        if (esVacio(cedula) || esVacio(nombre)) return Retorno.error1();
        if (!ci8Digitos(cedula)) return Retorno.error2();
        if (buscarUsuarioPorCI(cedula) != null) return Retorno.error3();
        
        Usuario usuario = new Usuario(cedula, nombre);
        usuarios.adicionarOrdenado(usuario);
        
        return Retorno.ok();
    }

    @Override
    public Retorno registrarBicicleta(String codigo, String tipo) {
        if (esVacio(codigo) || esVacio(tipo)) return Retorno.error1();
        if (!codigo6(codigo)) return Retorno.error2();
        if (parseTipo(tipo) == null) return Retorno.error3();
        if (buscarBicicletaPorCodigo(codigo) != null) return Retorno.error4();
        
        Bicicleta bici = new Bicicleta(codigo, parseTipo(tipo));
        deposito.adicionarOrdenado(bici);
        
        return Retorno.ok();
    }

    @Override
    public Retorno marcarEnMantenimiento(String codigo, String motivo) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno repararBicicleta(String codigo) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno eliminarEstacion(String nombre) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno asignarBicicletaAEstacion(String codigo, String nombreEstacion) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno alquilarBicicleta(String cedula, String nombreEstacion) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno devolverBicicleta(String cedula, String nombreEstacionDestino) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno deshacerUltimosRetiros(int n) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno obtenerUsuario(String ci) {
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
        
        //la nota es lo que hay que ignorar, se debe hacer de forma iterativa
        return Retorno.noImplementada();
    }

    @Override
    public Retorno informaciónMapa(String[][] mapa) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno listarBicicletasDeEstacion(String nombreEstacion) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno estacionesConDisponibilidad(int n) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno ocupacionPromedioXBarrio() {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno rankingTiposPorUso() {
        return Retorno.noImplementada();
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
