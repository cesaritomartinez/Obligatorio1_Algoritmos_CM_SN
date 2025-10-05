package dominio;


public class Alquiler implements Comparable<Alquiler>{

    public enum Estado { ACTIVO, EN_ESPERA_ANCLAJE, FINALIZADO }
    
    private static int contador = 1;

    private final int codigoAlquiler;
    private final String codigoBici;     // código de la bici alquilada
    private final String cedulaUsuario;  // CI del usuario
    private final String estacionOrigen; // estación desde donde se retiró

    private String estacionDestino;      // se fija al devolver (o al quedar esperando anclaje)
    private Estado estado;               // ACTIVO / EN_ESPERA_ANCLAJE / FINALIZADO

    public Alquiler(String codigoBici, String cedulaUsuario, String estacionOrigen) {
        this.codigoAlquiler = contador++;
        this.codigoBici = codigoBici;
        this.cedulaUsuario = cedulaUsuario;
        this.estacionOrigen = estacionOrigen;
        this.estacionDestino = null;
        this.estado = Estado.ACTIVO;
    }

    // ===== Getters =====
    public int getCodigoAlquiler()   { return codigoAlquiler; }
    public String getCodigoBici()     { return codigoBici; }
    public String getCedulaUsuario()  { return cedulaUsuario; }
    public String getEstacionOrigen() { return estacionOrigen; }
    public String getEstacionDestino(){ return estacionDestino; }
    public Estado getEstado()         { return estado; }

    public boolean esActivo()             { return estado == Estado.ACTIVO || estado == Estado.EN_ESPERA_ANCLAJE; }
    public boolean estaEsperandoAnclaje() { return estado == Estado.EN_ESPERA_ANCLAJE; }

    // Cuando intenta devolver y no hay anclaje libre: queda esperando en esa estación destino. 
    public void marcarEsperaAnclaje(String estacionDestino) {
        this.estacionDestino = estacionDestino;
        this.estado = Estado.EN_ESPERA_ANCLAJE;
    }

    // Cuando efectivamente ancla en la estación destino. */
    public void finalizar(String estacionDestino) {
        this.estacionDestino = estacionDestino;
        this.estado = Estado.FINALIZADO;
    }

    // Formato requerido por "deshacer últimos retiros": codigoBici#cedula#estacionOrigen */
    public String formatoDeshacer() {
        return codigoBici + "#" + cedulaUsuario + "#" + estacionOrigen;
    }
    
   @Override
    public int compareTo(Alquiler o) {
    return Integer.compare(this.codigoAlquiler, o.codigoAlquiler);
}


    @Override
    public String toString() {
        String dest = (estacionDestino == null ? "-" : estacionDestino);
        return codigoBici + "#" + cedulaUsuario + "#" + estacionOrigen + "#" + dest + "#" + estado;
    }
}
