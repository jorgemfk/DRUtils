package mx.dr.util;

import java.io.Serializable;

/**
 * Class that implements a generic <code>toString</code> method that uses refection.
 * Clase que implementa un metodo <code>toString</code> generico que usa refleccion.
 * @version 1.0 
 * @author Jorge Luis Martinez Ramirez
 * @since 01/01/2012
 */
public abstract class Base implements Serializable {
    /**
     * Using this method returns as refection <code>String</code> object attributes and their values.
     * <br/>
     * Usando reflecccion este metodo regresa como <code>String</code> los atributos del objeto y sus valores.
     * @return The object as a <code>String</code> with the name of their attributes and values/El objeto como <code>String</code> con el nombre de sus atributos y valores.
     */
    public String toString(){
        try {
            return ReflectionUtils.toReflectString(this);
        } catch (Exception ex) {
            //Logger.getLogger(Base.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
