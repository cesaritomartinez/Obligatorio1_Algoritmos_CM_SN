package dominio;

import tads.ListaSE;
import tads.ListaVaciaException;
import tads.PosFueraDeRangoException;

public class Estacion implements Comparable<Estacion>{

    private final String nombre;   // único en el sistema
    private final String barrio;
    private final int capacidad;   // > 0 (anclajes)

    // Bicicletas ancladas, SIEMPRE ordenadas por código (Bicicleta implements Comparable por código)
    private final ListaSE<Bicicleta> ancladas;

    // Colas con cédulas: espera para ALQUILAR y para ANCLAR (devolver)
    private final ListaSE<String> colaAlquiler;
    private final ListaSE<String> colaAnclaje;

    public Estacion(String nombre, String barrio, int capacidad) {
        this.nombre = nombre;
        this.barrio = barrio;
        this.capacidad = capacidad;
        this.ancladas = new ListaSE<>();
        this.colaAlquiler = new ListaSE<>();
        this.colaAnclaje = new ListaSE<>();
    }

    // ===== Getters básicos =====
    public String getNombre() { return nombre; }
    public String getBarrio() { return barrio; }
    public int getCapacidad() { return capacidad; }
    public int getOcupacion() { return ancladas.Longitud(); }
    public int getCuposLibres() { return capacidad - ancladas.Longitud(); }
    
    
    public boolean tieneAnclajeLibre() { return getCuposLibres() > 0; }

    // ===== Gestión de bicis ancladas (ordenadas por código) =====

    // Ancla la bici de forma ordenada si hay lugar. Devuelve true si ancló. 
    public boolean anclarBicicleta(Bicicleta bici) {
        if (!tieneAnclajeLibre()) return false;
        // La bici debe venir como DISPONIBLE (validado en Sistema)
        ancladas.adicionarOrdenado(bici);
        bici.setEstacionActual(this);
        return true;
    }

    // Toma la bici "menor código" (cabeza) para alquiler. Devuelve la bici o null si no hay. 
    public Bicicleta retirarDisponibleParaAlquiler() {
        if (ancladas.Vacia()) return null;
        Bicicleta primera = ancladas.Obtener(0);
        ancladas.Eliminar(0);
        primera.setEstacionActual(null);
        
        return primera;
    }

    // Lista los códigos anclados en ORDEN CRECIENTE, en un solo recorrido. 
    public String listarCodigosOrdenados() {
    String resultado = "";
    int n = ancladas.Longitud();
    for (int i = 0; i < n; i++) {
        Bicicleta b = ancladas.Obtener(i);
        if (i > 0) resultado += "|";
        resultado += b.getCodigo();
    }
    return resultado;
    }
    
    public void retirarBiciPorCodigo(String codigo) {
        int n = ancladas.Longitud();
    for (int i = 0; i < n; i++) {
        Bicicleta b = ancladas.Obtener(i);
        if (b.getCodigo().equals(codigo)) {
            ancladas.Eliminar(i);   // quita la bici de la estación
            b.setEstacionActual(null); 
            
            return; // listo: ya la retiramos
        }
    }
    }
    
   



    // ---- Espera para ALQUILAR en esta estación ----
    public void encolarEsperaAlquiler(String cedula) {
        colaAlquiler.Adicionar(cedula); // enqueue al final
    }
    public boolean hayEsperandoAlquiler() { return !colaAlquiler.Vacia(); }
    
    public String desencolarEsperaAlquiler() {
        if (colaAlquiler.Vacia()) return null;
        String ci = colaAlquiler.Obtener(0);
        colaAlquiler.Eliminar(0);
        return ci;
    }

    // ---- Espera para ANCLAR (devolver) en esta estación ----
    public void encolarEsperaAnclaje(String cedula) {
        colaAnclaje.Adicionar(cedula);
    }
    public boolean hayEsperandoAnclaje() { return !colaAnclaje.Vacia(); }
    
    public String desencolarEsperaAnclaje() {
        if (colaAnclaje.Vacia()) return null;
        String ci = colaAnclaje.Obtener(0);
        colaAnclaje.Eliminar(0);
        return ci;
    }

    // ===== Apoyos para reglas del enunciado =====

    /** True si NO hay bicis ancladas NI colas (útil para eliminar estación). */
    public boolean sinPendientes() {
        return ancladas.Vacia() && colaAlquiler.Vacia() && colaAnclaje.Vacia();
    }

    // % ocupación de la estación (enteros redondeados). 
    public int porcentajeOcupacion() {
        if (capacidad == 0) return 0;
        double p = (getOcupacion() * 100.0) / capacidad;
        return (int)Math.round(p);
    }
    
    @Override
    public int compareTo(Estacion e) {
        return this.nombre.compareTo(e.nombre);
    }

    @Override
    public String toString() {
        
        return nombre + "#" + barrio + "#" + getOcupacion() + "/" + capacidad;
    }

    
}
