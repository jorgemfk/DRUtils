package mx.dr.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
/**
 * Class with several utilities.
 * Clase con utilerias varias.
 * @version 1.0 
 * @author Jorge Luis Martinez Ramirez
 * @since 01/01/2012
 */
public class DRGeneralUtils{
    /**
     * formato de fecha.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    /**
     * formato de fecha con hora.
     */
    private static final SimpleDateFormat DATE_HOUR_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

    private DRGeneralUtils() {
    }

    /**
     * Method to format a date <code>dd/MM/yyyy hh:mm:ss</code>.
     * <br/>
     * M&eacute;todo para formatear una fecha a <code>dd/MM/yyyy hh:mm:ss</code>.
     * @param date Date to format / Fecha a formatear
     * @return formatted date / Fecha formateada
     */
    public static String formatDateHour(Date date) {
        return formatDate(date, DATE_HOUR_FORMAT);
    }
    /**
     * Method to format a date <code>dd/MM/yyyy</code>.
     * <br/>
     * M&eacute;todo para formatear una fecha a <code>dd/MM/yyyy</code>.
     * @param date Date to format / Fecha a formatear
     * @return formatted date / Fecha formateada
     */
    public static String formatDate(Date date) {
        return formatDate(date, DATE_FORMAT);
    }
    /**
     * Method to remove html tags from text.
     * <br/>
     * Metodo que quita las etiquetas html de un texto.
     * @param input Texto con etiquetas html
     * @return Texto sin etiquetas html
     */
    public static String formatHTML(String input) {

        String nohtml = input.replaceAll("\\<.*?>", "");
        return nohtml;
    }
    
    /**
     * Method that applies the desired format to a date.
     * <br/>
     * Metodo que aplica el formato deseado a una fecha.
     * @param date desired date to format / fecha que se desea formatear.
     * @param format date formatter / formateador de fecha.
     * @return Formated date as <code>String</code> / Fecha formateada como <code>String</code>
     */
    public static String formatDate(Date date, SimpleDateFormat format) {
        String fdate = "";
        if (date != null) {
            fdate = format.format(date);
        }
        return fdate;
    }
    /**
     * Method that translates the resources and properties.
     * <br/>
     * Metodo que traduce los recursos como propiedades.
     * @param bundle Resource bundle / paquete de recursos.
     * @return Crumbling properties / propiedades desmenuzadas.
     */
    public synchronized static Properties convertToProperties(ResourceBundle bundle) {
        Properties properties = new Properties();
        Enumeration<String> keys = bundle.getKeys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            properties.put(key, bundle.getString(key));
        }
        return properties;
    }
    /**
     * Method a text encoding value that translates MD5.
     * <br/>
     * Metodo que codifica un texto en su valor MD5.
     * @param chain message to be encoded / mensage a codificar.
     * @return encoded message / mensaje codificado.
     */
    public synchronized static String stringToMD5(String chain) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(chain.getBytes());
            byte messageDigest[] = algorithm.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Method that adds days to a date.
     * <br/>
     * Metodo que suma dias a una fecha.
     * @param dplus days to add / dias a sumar.
     * @param dday original date / fecha original.
     * @return Date result / fecha resultado.
     */
    
    public synchronized static Date addDays(int dplus, Date dday) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, dplus);
        System.out.println("initd " + dday + " enddate " + c.getTime());
        return c.getTime();
    }
    /**
     * Calculates number of days between two days.
     * <br/>
     * Calcula en numero de dias comprendido entre dos fechas.
     * @param init init date / fecha inicio.
     * @param end end date / fecha fin.
     * @return number of days / numero de dias.
     */
    public static int days(Date init, Date end) {
        long diferenciaMils = end.getTime() - init.getTime();
        long segundos = diferenciaMils / 1000;
        int dias = (int) (segundos / 86400);
        return dias;
    }
    /**
     * Transform the date of entry into the BREAKDOWN of its parts.
     * <br/>
     * Transforma la fecha de entrada en el desgloce de sus partes.
     * @param end date entry / fecha entrada.
     * @return date broken down into its parts / fecha desglosada en sus partes.
     */
    public static String endTime(Date end) {
        StringBuffer sb = new StringBuffer();
        Date now = Calendar.getInstance().getTime();
        long diferenciaMils = end.getTime() - now.getTime();
        //obtenemos los segundos
        long segundos = diferenciaMils / 1000;
        //obtenemos las horas
        long dias = segundos / 86400;
        segundos -= dias * 86400;
        long horas = segundos / 3600;
        //restamos las horas para continuar con minutos
        segundos -= horas * 3600;
        //igual que el paso anterior
        long minutos = segundos / 60;
        segundos -= minutos * 60;
        return sb.append(dias).append("dias ").append(horas).append("h ").append(minutos).append("m ").append(segundos).append("s").toString();
    }
    /**
     * Read a web address and get its content.
     * <br/>
     * Lee una direccion web y obtiene su contenido.
     * @param uri URL to read / URL a leer.
     * @return content of the web address / contenido de la direccion web.
     * @throws Exception if an error occurs / si ocurre un error.
     */
    public static String readURL(String uri) throws Exception {
        URL url = new URL(uri);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String inputLine;
        StringBuffer sf = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {

            sf.append(inputLine);
        }
        in.close();
        return sf.toString();
    }
    /**
     * Turn the parameters introduced as  <code> GET </code> request to a map of properties.
     * <br/>
     * Transforma los parametros introducidos como peticion <code>GET</code> a un mapa de propiedades.
     * @param paramString site parameters as petitioned <code> GET </code> / Parametros web como peticion <code>GET</code>
     * @return Map with site properties / Mapa con las propiedades web.
     */
    public static Map<String, String> uriparams2Params(String paramString) {
        Map params = new HashMap();
        for (String s : paramString.split("&")) {
            String[] p = s.split("=");
            params.put(p[0], p[1]);

        }
        System.out.println(params);
        System.out.println(params.values());

        return params;
    }
    /**
     * Truncates a text to maximum number of characters.
     * <br/>
     * Trunca un texto al numero de caracteres maximos.
     * @param s text candidate to truncate / texto candidato a truncar.
     * @param max maximum characters to text / caracteres maximos para el texto.
     * @return text truncated / texto truncado.
     */
    public static String truncate(String s, int max) {
        if (s == null) {
            return null;
        }
        if (s.length() > max) {
            return s.substring(0, max - 1);
        }
        return s;
    }
    
    /**
     * Deliver an unlimited number of objects as under.
     * <br/>
     * Entrega un numero indefinido de objetos como arreglo.
     * @param o multiple entry of objects / entrada multiple de objetos.
     * @return array of objects / arreglo de objetos
     */
    public static Object[] argsAsArray(Object... o) {
        return o;
    }
    
    public static String remove2(String input) {
        
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
       
        Pattern pattern = Pattern.compile("\\p{ASCII}+");
        return pattern.matcher(normalized).replaceAll("");
    }
    
    public static String remove1(String input) {
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i=0; i<original.length(); i++) {
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }
        return output;
    }
    
    public static void main(String[] s){
    	System.out.println(remove1("ávenida ñoño")+new Date(0));
    	String var="|@yesenia@|19.0101816666667@|-99.1708116666667|@";
    	String[] flocations;
    	if(var.startsWith("|@")){
    	for(String friend:var.split("\\|@")){
    		if(friend.trim().length()>1){
    		flocations= friend.split("@\\|");
    		for(String part:flocations){
    			System.out.println("a: "+part);
    		}
    		}
    	}
    	}
    }
}
