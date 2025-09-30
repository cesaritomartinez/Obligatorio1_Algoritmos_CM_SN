package tads;

/* Clase ListaSE: Implementacion del TDA Lista usando
 * nodos simplemente enlazados, con apuntador al primer nodo 
 */
public class ListaSE<T> implements ILista<T> {

    protected NodoSE<T> cabeza;
    protected int longitud;

    public ListaSE() {
        cabeza = null;
        longitud = 0;
    }

    @Override
    public void Adicionar(T x) {
        NodoSE<T> nodo_nuevo = new NodoSE<T>(x);
        if (cabeza == null)
            cabeza = nodo_nuevo;
        else{
            NodoSE<T> actual = cabeza; 
            while (actual.getSiguiente()!= null)
                actual = actual.getSiguiente();
            actual.setSiguiente(nodo_nuevo);
        }
        longitud++;
        
    }

    @Override
    public void Insertar(T x, int pos) throws PosFueraDeRangoException {
        if (pos < 0 || pos >= longitud) throw new PosFueraDeRangoException();
        NodoSE<T> nodo_nuevo; // NodoSE<T> nodo_nuevo = new NodoSE<T>(x);
        if (pos == 0){
            nodo_nuevo = new NodoSE<T>(x, cabeza); // nodo_nuevo.setSguiente(cabeza);
            cabeza = nodo_nuevo;
        }
        else {
             NodoSE<T> actual = cabeza; 
             nodo_nuevo = new NodoSE<T>(x);
             for (int i=0; i< pos-1; i++)
                  actual = actual.getSiguiente();
             nodo_nuevo.setSiguiente(actual.getSiguiente());
             actual.setSiguiente(nodo_nuevo);
        }
        longitud++;
        
    }

    @Override
    public T Obtener(int pos){
        if (Vacia()) throw new ListaVaciaException();
        NodoSE<T> actual = cabeza;
        if (pos < longitud && pos >= 0){
            for (int i=0; i< pos; i++)
                actual = actual.getSiguiente();
            return actual.getDato();
        }
        else 
            throw new PosFueraDeRangoException();

    }

    @Override
    public void Eliminar(int pos) throws PosFueraDeRangoException, ListaVaciaException {
         if (pos < 0 || pos >= longitud) throw new PosFueraDeRangoException();
         if (pos == 0){
             cabeza = cabeza.getSiguiente();
         }
         else{
            NodoSE<T> actual = cabeza;
            for (int i=0; i< pos-1; i++)
                actual = actual.getSiguiente();
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
         }
         longitud--;
    }

    @Override
    public int Longitud() {
        return longitud;
    }

    @Override
    public boolean Vacia() {
        return (longitud == 0);

    }

}
