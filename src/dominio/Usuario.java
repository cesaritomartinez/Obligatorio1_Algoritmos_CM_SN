package dominio;

public class Usuario implements Comparable<Usuario> {

    private final String cedula;   // 8 dígitos, única en el sistema
    private String nombre;

    // Cantidad de alquileres completados (para reporte 3.10)
    private int alquileresCompletados;

    // Para saber si tiene un alquiler activo (código de bici actual o null)
    private String codigoBiciActual;

    public Usuario(String cedula, String nombre) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.alquileresCompletados = 0;
        this.codigoBiciActual = null;
    }

    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public void setNombre(String nuevoNombre) { this.nombre = nuevoNombre; }

    public int getAlquileresCompletados() { return alquileresCompletados; }
    public boolean tieneAlquilerActivo() { return codigoBiciActual != null; }
    public String getCodigoBiciActual() { return codigoBiciActual; }

    // Lo usa Sistema al alquilar/devolver
    public void iniciarAlquiler(String codigoBici) { this.codigoBiciActual = codigoBici; }
    public void finalizarAlquiler() {
        if (this.codigoBiciActual != null) {
            this.codigoBiciActual = null;
            this.alquileresCompletados++;
        }
    }

    @Override
    public int compareTo(Usuario o) {
        int cmp = this.nombre.compareToIgnoreCase(o.nombre);
        return (cmp != 0) ? cmp : this.cedula.compareTo(o.cedula);
    }


    @Override
    public String toString() {
        // Requerido por 3.1 / 3.2: "nombre#cedula"
        return nombre + "#" + cedula;
    }
}

