/*
 * PosFueraDeRangoException.java
 *
 * Created on 31 de octubre de 2007, 0:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tads;

/**
 *
 * @author DDC Tecnicas de Programacion
 */
public class PosFueraDeRangoException extends RuntimeException {
    
    /** Creates a new instance of ExceptionPosFueraDeRango */
    public PosFueraDeRangoException() {
        super("Posicion fuera de rango");
    }
    
}
