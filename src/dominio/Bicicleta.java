package dominio;


public class Bicicleta {
    
    private final String codigo;   // 6 caracteres, único
    private final String tipo;     // "URBANA" | "MOUNTAIN" | "ELECTRICA"
    private String estado;         // "DEPOSITO" | "MANTENIMIENTO" | "DISPONIBLE"

    public Bicicleta(String codigo, String tipo) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.estado = "DEPOSITO";  // al registrarla queda en depósito
    }

    public String getCodigo() { return codigo; }
    public String getTipo()   { return tipo;   }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        // Útil para 3.3: "codigo#tipo#estado"
        return codigo + "#" + tipo + "#" + estado;
    }
}
