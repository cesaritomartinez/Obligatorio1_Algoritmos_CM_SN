package tads;


import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author yanete
 */
public class PilaSE<T> {
    private NodoSE<T> tope;
    

    public PilaSE() {
        tope = null;
    }

    public void apilar(T dato) {
       NodoSE<T> nuevo_nodo = new NodoSE<T>(dato);
       nuevo_nodo.setSiguiente(tope);
       tope = nuevo_nodo;
    }

    public T desapilar() {
       T dato = null;
       if (!estaVacia()){
           dato = tope.getDato();
           tope = tope.getSiguiente();
       }
       return dato;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public T getTope() {
       return tope != null ? tope.getDato() : null;
       
    }
    
    /*Metodo utilizado para la visualización de la Pila*/
    List<T> obtenerElementos() {
        List<T> elementos = new ArrayList<>();
        NodoSE<T> actual = tope;
        while (actual != null) {
            elementos.add(actual.getDato());
            actual = actual.getSiguiente();
        }
        return elementos;
    }
    
    private int CantidadElementos(){
        int cont = 0;
        PilaSE<T> aux = new PilaSE<T>();
        while (!estaVacia()) {
            cont ++;
            aux.apilar(desapilar());
        }
        while (!aux.estaVacia()){
            this.apilar(aux.desapilar());
        }
        
        return cont;  
    }
    
    private PilaSE<T> copiarPila(){
        PilaSE<T> aux = new PilaSE<T>();
        PilaSE<T> resultado = new PilaSE<T>();
        while (!estaVacia()) 
           aux.apilar(desapilar());
        while (!aux.estaVacia()){
           T dato = aux.desapilar();
           resultado.apilar(dato);
           apilar(dato);
        }
        return resultado;
      
    }
    
    private void intercambiarTope(){
        T tope = desapilar();
        T subTope = desapilar();
        apilar(tope);
        apilar(subTope);
    } 
    
    private void concatenar (PilaSE<T> otraPila){
       PilaSE<T> aux = new PilaSE<T>();
       while (!estaVacia()) 
         aux.apilar(desapilar());
       while (!otraPila.estaVacia()) 
         aux.apilar(otraPila.desapilar());
       while (!aux.estaVacia()) 
         apilar(aux.desapilar()); 
    }
    
    private void invertir(){
        ColaSE<T> aux = new ColaSE<T>();
         while (!estaVacia())
             aux.encolar(desapilar());
         while (!aux.estaVacia()) 
             apilar(aux.desencolar());
    }  
}

