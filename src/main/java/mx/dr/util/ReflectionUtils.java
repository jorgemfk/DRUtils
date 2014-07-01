package mx.dr.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class that implements several utilities of refection. <br/> Clase que
 * implementa varias utilerias de refeccion.
 *
 * @version 1.0
 * @author Jorge Luis Martinez Ramirez
 * @since 01/01/2012
 */
public class ReflectionUtils {

    /**
     * private constructor prevents from make a instance
     */
    private ReflectionUtils() {
    }

    /**
     * Method that gets the attributes and values of any object and writes a
     * <code> String </ code>.
     * <br/>
     * Metodo que obtiene los atributos y valores de un objeto cualquiera y los escribe en un
     * <code>String</code>.
     *
     * @param obj Object which give the values of the attributes / Objeto del
     * cual se obtienen los valores de los atributos.
     * @return
     * <code> String </ code> with the BREAKDOWN of attributes and values ​​of the object /
     * <code>String</code> con el desgloce de atributos y valores del objeto.
     * @throws Exception If there is an operation not allowed / Si ocurre una
     * operacion no permitida.
     */
    public static String toReflectString(Object obj) throws Exception {
        Class myClass = obj.getClass();
        StringBuffer sf = new StringBuffer(myClass.getName());
        for (Field field : myClass.getDeclaredFields()) {
            field.setAccessible(true);
            sf.append("[").append(field.getName()).append(" = ").append(field.get(obj)).append("]");
        }
        return sf.toString();
    }

    /**
     * Method that gets the attributes of an object. <br/> Metodo que obtiene
     * los atributos de un objeto.
     *
     * @param o Object which you will obtain the list of attributes/ Objeto del
     * cual se obtendran la lista de atributos.
     * @return List of attribute names / Lista de los nombres de los atributos
     */
    public static List<String> atributes(final Object o) {
        final List<String> metodos = new ArrayList<String>();
        for (Method method : o.getClass().getMethods()) {
            if (method.getName().startsWith("set")) {
                metodos.add(method.getName().substring(3));
            }
        }
        return metodos;
    }

    /**
     * Method that copies the attributes of two objects, attributes should be
     * called just copied and type must be the same. <br/> Metodo que copia los
     * atributos entre dos Objetos, los atributos copiados se deben llamar igual
     * y su tipo debe ser el mismo.
     *
     * @param in Input Object / Objeto de entrada
     * @param out Output Object (with attributes copied) / Objeto de salida (Con
     * los atributos copiados)
     * @throws Exception If there is an operation not allowed / Si ocurre una
     * operacion no permitida.
     */
    public static void copyProperties(Object in, Object out) throws Exception {
        Object valor = null;
        Class clase;
        for (String atributo : ReflectionUtils.atributes(in)) {
            //atributo =(""+atributo.charAt(0)).toUpperCase()+atributo.substring(1);
            try {
                valor = in.getClass().getMethod("get" + atributo).invoke(in);

                if (valor != null) {
                    clase = valor.getClass();
                    if (valor instanceof Timestamp) {
                        out.getClass().getMethod("set" + atributo, Date.class).invoke(out, valor);
                    } else if (valor instanceof java.sql.Date) {
                        out.getClass().getMethod("set" + atributo, Date.class).invoke(out, valor);
                    } else {
                        out.getClass().getMethod("set" + atributo, clase).invoke(out, valor);
                    }
                }
            } catch (Exception e) {
            }
            /*
             * Class myClass=in.getClass();
             *
             * for(Field field:myClass.getDeclaredFields()){
             * field.setAccessible(true); try{ genericSet(out, field.getName(),
             * field.get(in), field.getType()); } catch (Exception e) {
             * System.out.print(e.getMessage()); }
            }
             */
        }
    }

    /**
     * Gets an instance of the attribute found by name. <br/> Obtiene una
     * instancia del atributo encontrado por su nombre.
     *
     * @param name Name of attribute to find / Nombre del atributo a encontrar.
     * @param myClass Class where the attribute should be sought / Clase en
     * donde se debe buscar el atributo.
     * @return Instance of the attribute found / instancia del atributo
     * encontrado.
     * @throws Exception Exception If there is an operation not allowed / Si
     * ocurre una operacion no permitida.
     */
    public static Field getField(String name, Class myClass) throws Exception {
        for (Field field : myClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    /**
     * Applies a generic getter based on the attribute name. The target class
     * must be a
     * <code>get*</code> to the desired attribute. <br/> Aplica un getter
     * generico basado en el nombre del atributo. En la clase fuente debe
     * existir un
     * <code>get*</code> para el atributo deseado.
     *
     * @param instance Object you will get the attribute value / Objeto del que
     * se obtendra el valor del atributo.
     * @param fieldName Attribute Name you will get the value / Nombre del
     * atributo del que obtendra el valor.
     * @return The attribute value / El valor del atributo.
     * @throws Exception Exception If there is an operation not allowed / Si
     * ocurre una operacion no permitida.
     */
    public static Object genericGet(Object instance, String fieldName) throws Exception {
        //Field field = instance.getClass().getField(fieldName);
        //field.setAccessible(true);
        //return field.get(instance);
        Method method = instance.getClass().getMethod("get" + (fieldName.charAt(0) + "").toUpperCase() + fieldName.substring(1));
        return method.invoke(instance);
    }

    /**
     * Applies a generic
     * <code>setter</code> based on the attribute name. The target class must be
     * a
     * <code>set*</code> to the desired attribute. <br/> Aplica un
     * <code>setter</code> generico basado en el nombre del atributo. En la
     * clase fuente debe existir un
     * <code>set*</code> para el atributo deseado.
     *
     * @param instance Object you will set the attribute value / Objeto del que
     * se asignara el valor del atributo.
     * @param fieldName Attribute Name you will set the value / Nombre del
     * atributo del que se asignara el valor.
     * @param value The attribute value / El valor del atributo.
     * @throws Exception Exception If there is an operation not allowed / Si
     * ocurre una operacion no permitida.
     */
    public static void genericSet(Object instance, String fieldName, Object value) throws Exception {
        //Field field = instance.getClass().getField(fieldName);
        //field.setAccessible(true);
        //field.set(instance , value);
        if (value != null) {
            Method method = instance.getClass().getMethod("set" + (fieldName.charAt(0) + "").toUpperCase() + fieldName.substring(1), value.getClass());
            method.invoke(instance, value);
        }
    }
}
