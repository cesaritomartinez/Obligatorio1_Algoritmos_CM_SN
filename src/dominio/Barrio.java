package dominio;

public class Barrio implements Comparable<Barrio> {
    private final String nombre;   
    private int capacidadTotal;    
    private int ancladas;          

    public Barrio(String nombre) {
        this.nombre = nombre;
        this.capacidadTotal = 0;
        this.ancladas = 0;
    }

    // getters
    public String getNombre() { return nombre; }
    public int getCapacidadTotal() { return capacidadTotal; }
    public int getAncladas() { return ancladas; }

    // acumuladores
    public void sumarCapacidad(int c)   { capacidadTotal += c; }
    public void restarCapacidad(int c)  { capacidadTotal -= c; }
    public void sumarAnclada()            { ancladas++; }
    public void restarAnclada()            { ancladas --; }

   

    // ordenar alfabéticamente por nombre (case-insensitive)
    @Override
    public int compareTo(Barrio o) {
        return this.nombre.compareToIgnoreCase(o.nombre);
    }
}
