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
public class ColaSE<T> {
    private NodoSE<T> frente;
    private NodoSE<T> fondo;

 
    public ColaSE() {
        frente = null;
        fondo = null;
    }

    public void encolar(T dato) {
      NodoSE<T> nuevo_nodo = new NodoSE<T>(dato);
      if (estaVacia()) 
          frente = nuevo_nodo;
      else
          fondo.setSiguiente(nuevo_nodo);
      fondo = nuevo_nodo;
    }
       
    public T desencolar() {
      T dato = null;
      if (!estaVacia()){
          dato = frente.getDato();
          frente = frente.getSiguiente();
          if (frente == null)
              fondo = null;
      }
      return dato;
    }

    public boolean estaVacia() {
        return frente == null && fondo == null;
    }

    public T getFrente() {
       /*if (frente != null)
           return frente.getDato();
       else return null;*/
       
       return frente != null ? frente.getDato() : null;
       
    }
    
    public T getFondo() {
        return fondo != null ? fondo.getDato() : null;
    }
    
    /*Metodo utilizado para la visualización de la Cola*/
    List<T> obtenerElementos() {
        List<T> elementos = new ArrayList<>();
        NodoSE<T> actual = frente;
        while (actual != null) {
            elementos.add(actual.getDato());
            actual = actual.getSiguiente();
        }
        return elementos;
    }
}

