/*
 * ListaVaciaException.java
 *
 * Created on 31 de octubre de 2007, 0:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tads;

/**
 *
 * @author DDC Tecnicas de Programaci�n
 */
public class ListaVaciaException extends RuntimeException {
    
    /** Creates a new instance of ExceptionListaVacia */
    public ListaVaciaException() {
        super("Lista Vacia");
    }
}
