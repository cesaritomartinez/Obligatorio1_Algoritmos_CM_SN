package sistemaAutogestion;

//Agregar aquí nombres y números de estudiante de los integrantes del equipo

public class Sistema implements IObligatorio {
    
    

    @Override
    public Retorno crearSistemaDeGestion() {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno registrarEstacion(String nombre, String barrio, int capacidad) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno registrarUsuario(String cedula, String nombre) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno registrarBicicleta(String codigo, String tipo) {
        return Retorno.noImplementada();
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
    public Retorno obtenerUsuario(String cedula) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno listarUsuarios() {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno listarBicisEnDeposito() {
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
