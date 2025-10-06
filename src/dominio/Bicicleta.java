package dominio;

public class Bicicleta implements Comparable<Bicicleta> {


    public enum Tipo   { URBANA, MOUNTAIN, ELECTRICA }
    public enum Estado { MANTENIMIENTO, DISPONIBLE, ALQUILADA }

    private final String codigo;         // 6 caracteres, único
    private final Tipo tipo;
    private Estado estado;               // inicia en DEPOSITO
    private String motivoMantenimiento;  // null cuando no aplica
    private Estacion estacionActual; // null si esta en deposito

    public Bicicleta(String codigo, Tipo tipo) {
        // Se asume validado externamente
        this.codigo = codigo;
        this.tipo = tipo;
        this.estado = Estado.DISPONIBLE;
        this.estacionActual = null;
        this.motivoMantenimiento = null;
    }

    public String getCodigo() { return codigo; }
    public Tipo getTipo() { return tipo; }
    public Estado getEstado() { return estado; }
    public String getMotivoMantenimiento() { return motivoMantenimiento; }
    public Estacion getEstacionActual() {return estacionActual;}
    
    public void setEstacionActual(Estacion e) { this.estacionActual = e; }

    
    public void marcarEnMantenimiento(String codigo, String motivo) {
        this.estado = Estado.MANTENIMIENTO;
        this.motivoMantenimiento = motivo;
    }

    
    public void reparar() {
        this.estado = Estado.DISPONIBLE;
        this.motivoMantenimiento = null;
    }

    public void setEstado(Estado nuevo) {
        this.estado = nuevo;
    }
    
    public void setMotivoMantenimiento(String motivo) {
    // guarda null si viene vacío; si no, lo deja prolijo
    if (motivo == null) {
        this.motivoMantenimiento = null;
    } else {
        String m = motivo;
        this.motivoMantenimiento = m.isEmpty() ? null : m;
    }
}


    @Override
    public int compareTo(Bicicleta o) {
        return this.codigo.compareTo(o.codigo);
    }

    @Override
    public String toString() {
        return codigo + "#" + tipo + "#" + estado;
        // Si querés incluir el motivo cuando está en mantenimiento:
        // return (estado == Estado.MANTENIMIENTO && motivoMantenimiento != null)
        //        ? codigo + "#" + tipo + "#" + estado + "#" + motivoMantenimiento
        //        : codigo + "#" + tipo + "#" + estado;
    }
}
