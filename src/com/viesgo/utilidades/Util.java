package com.viesgo.utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Util {
    
	private static Log logger = LogFactory.getFactory().getInstance(Util.class);
	
	public Util() {
	}
	
	public static boolean isNumeric(String inputData) {
		  return inputData.matches("[-+]?\\d+(\\.\\d+)?");
		}
	
	public static String fechaHoyH24() {
		Calendar cal = Calendar.getInstance();
		int dia = cal.get(Calendar.DATE);
		int mes = cal.get(Calendar.MONTH);
		int anio = cal.get(Calendar.YEAR);
		int hora = cal.get(Calendar.HOUR_OF_DAY);
		int minuto = cal.get(Calendar.MINUTE);
		int segundo = cal.get(Calendar.SECOND);
		
		// La clase Calendar devuelve el mes en formato M y toma enero con valor
		// 0.
		String mesHoy = "";
		if (mes == 0)
			mesHoy = "01";
		else if (mes == 1)
			mesHoy = "02";
		else if (mes == 2)
			mesHoy = "03";
		else if (mes == 3)
			mesHoy = "04";
		else if (mes == 4)
			mesHoy = "05";
		else if (mes == 5)
			mesHoy = "06";
		else if (mes == 6)
			mesHoy = "07";
		else if (mes == 7)
			mesHoy = "08";
		else if (mes == 8)
			mesHoy = "09";
		else if (mes == 9)
			mesHoy = "10";
		else if (mes == 10)
			mesHoy = "11";
		else
			mesHoy = "12";
		String strDia = "";
		if (dia < 10) {
			strDia = "0" + dia;
		} else {
			strDia = String.valueOf(dia);
		}
		String fecha = "";
		fecha = anio + mesHoy + strDia + 
				Util.aTexto(String.valueOf(hora), 2, "0", true)+
				Util.aTexto(String.valueOf(minuto), 2, "0", true)+
				Util.aTexto(String.valueOf(segundo), 2, "0", true);
		return fecha; 
	}
	

	/**	Formatea el campo pasado como parámetro con los datos introducidos
	 * @param pValor	String a formatear
	 * @param pLongitud Longitud final del String
	 * @param pCaracter Caracter que se utiliza de relleno
	 * @param pPrefijo	Indica si el relleno se concatena al principio o al final
	 * @return	Strin formateado
	 */
	public static String aTexto(String pValor,int pLongitud, String pCaracter,boolean pPrefijo){
		try{
			String resultado = "";
			if (!pPrefijo){
				if(pValor.trim().length()>pLongitud) resultado = pValor.trim().substring(0,pLongitud);
				else resultado = pValor.trim();
			}
			for(int i=0;i<(pLongitud - pValor.trim().length());i++){
				resultado = resultado.concat(pCaracter);
			}
			if (pPrefijo){
				if(pValor.trim().length()>pLongitud) resultado = pValor.trim().substring(0,pLongitud);
				else resultado = resultado.concat(pValor.trim());
			}
			return resultado;
		}catch(Exception e){return pValor;}
	}

	
	/**
	 * Recupera la fecha de hoy en formato dd/mm/yyyy.
	 * @return la fecha de hoy.
	 */
	public static String fechaHoyDDMMYYYY(){
		Calendar cal = Calendar.getInstance();
		int dia = cal.get(Calendar.DATE);
		int mes = cal.get(Calendar.MONTH);
		int anio = cal.get(Calendar.YEAR);
		//La clase Calendar devuelve el mes en formato M y toma enero con valor 0.
		String mesHoy = "";
		if(mes == 0)
			mesHoy = "01";
		else if(mes == 1)
			mesHoy = "02";
		else if(mes == 2)
			mesHoy = "03";
		else if(mes == 3)
			mesHoy = "04";
		else if(mes == 4)
			mesHoy = "05";
		else if(mes == 5)
			mesHoy = "06";
		else if(mes == 6)
			mesHoy = "07";
		else if(mes == 7)
			mesHoy = "08";
		else if(mes == 8)
			mesHoy = "09";
		else if(mes == 9)
			mesHoy = "10";
		else if(mes == 10)
			mesHoy = "11";
		else
			mesHoy = "12";
		String strDia = "";
		if(dia < 10){
			strDia = "0" + dia;
		}
		else{
			strDia = String.valueOf(dia);
		}
		String fecha = "";
		fecha = strDia + "/" + mesHoy + "/" + anio;
		return fecha;
	}
	
	/**
	 * Recupera la fecha de hoy en formato yyyy/mm/dd.
	 * @return la fecha de hoy.
	 */
	public static String fechaHoyYYYYMMDD(){
		Calendar cal = Calendar.getInstance();
		int dia = cal.get(Calendar.DATE);
		int mes = cal.get(Calendar.MONTH);
		int anio = cal.get(Calendar.YEAR);
		//La clase Calendar devuelve el mes en formato M y toma enero con valor 0.
		String mesHoy = "";
		if(mes == 0)
			mesHoy = "01";
		else if(mes == 1)
			mesHoy = "02";
		else if(mes == 2)
			mesHoy = "03";
		else if(mes == 3)
			mesHoy = "04";
		else if(mes == 4)
			mesHoy = "05";
		else if(mes == 5)
			mesHoy = "06";
		else if(mes == 6)
			mesHoy = "07";
		else if(mes == 7)
			mesHoy = "08";
		else if(mes == 8)
			mesHoy = "09";
		else if(mes == 9)
			mesHoy = "10";
		else if(mes == 10)
			mesHoy = "11";
		else
			mesHoy = "12";
		String strDia = "";
		if(dia < 10){
			strDia = "0" + dia;
		}
		else{
			strDia = String.valueOf(dia);
		}
		String fecha = "";
		fecha = anio + "/" + mesHoy + "/" + strDia;
		return fecha;
	}

	/**
	 * Añade a una cadena de texto un número de caracteres determinados.
	 * @param cadena la cadena de texto a la que se van a añadir los caracteres.
	 * @param numeroCaracteres el número de caracteres a añadir.
	 * @param caracter el caracter a añadir.
	 * @param direccion "R" si los caracteres se van a añadir por la derecha y "L" si se van a añadir por la izquierda.
	 * @return la cadena de texto con los caracteres nuevos.
	 */
	public static String rellenaCaracteres(String cadena, int numeroCaracteres, String caracter, String direccion){
		try{
			if(caracter.equals("")){
				caracter =  " ";
			}
			if(direccion.equals("R")){
				for(int i=0;i<numeroCaracteres;i++){
					cadena = cadena + caracter;
				}
			}
			else{
				for(int i=0;i<numeroCaracteres;i++){
					cadena = caracter + cadena;
				}
			}
		}
		catch(Exception e){
			logger.error(e.toString());
		}
		return cadena;
	}

	/**
	 * Elimina de una cadena de texto unos determinados caracteres por la izquierda.
	 * @param cadena la cadena de texto.
	 * @param caracter el caracter a eliminar.
	 * @return la cadena de texto sin los caracteres eliminados.
	 */
	public static String eliminaCaracteresIzq(String cadena, String caracter){
		
		int posicion = 0;
		int longitud = 0;
		
		try{
			
			longitud = cadena.length();
			for (int i=0; i<longitud;i++){
				posicion = cadena.indexOf(caracter);
				if (posicion == 0){
					cadena = cadena.substring(posicion+1);
				}
				else{
					break;
				}
			}
			
			return cadena;
		}
		catch(Exception e){
			logger.error(e.toString());
			return null;
		}
	}
	
	/**
	 * Elimina de una cadena de texto unos determinados caracteres por la derecha.
	 * @param cadena la cadena de texto.
	 * @param caracter el caracter a eliminar.
	 * @return la cadena de texto sin los caracteres eliminados.
	 */
	public static String eliminaCaracteresDrcha(String cadena, String caracter){
		
		int posicion = 0;
		int longitud = 0;
		
		try{
			
			longitud = cadena.length();
			for (int i=0; i<longitud;i++){
				posicion = cadena.lastIndexOf(caracter);
				if (posicion == cadena.length()-1){
					cadena = cadena.substring(0, posicion);
				}
				else{
					break;
				}
			}
			
			return cadena;
		}
		catch(Exception e){
			logger.error(e.toString());
			return null;
		}
	}

	 /** Recupera la fecha con horas min. y segundos en formato yyyymmddhhmmss.
	 * @return la fecha de hoy.
	 */
	public static String fechaCompleta(){
		Calendar cal = Calendar.getInstance();
		
		int dia = cal.get(Calendar.DATE);
		int mes = cal.get(Calendar.MONTH);
		int anio = cal.get(Calendar.YEAR);
		int hora = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int seg = cal.get(Calendar.SECOND);
		
		//La clase Calendar devuelve el mes en formato M y toma enero con valor 0.
		String mesHoy = "";
		if(mes == 0)
			mesHoy = "01";
		else if(mes == 1)
			mesHoy = "02";
		else if(mes == 2)
			mesHoy = "03";
		else if(mes == 3)
			mesHoy = "04";
		else if(mes == 4)
			mesHoy = "05";
		else if(mes == 5)
			mesHoy = "06";
		else if(mes == 6)
			mesHoy = "07";
		else if(mes == 7)
			mesHoy = "08";
		else if(mes == 8)
			mesHoy = "09";
		else if(mes == 9)
			mesHoy = "10";
		else if(mes == 10)
			mesHoy = "11";
		else
			mesHoy = "12";
		
		//Día 
		String strDia = "";
		if(dia < 10){
			strDia = "0" + dia;
		}
		else{
			strDia = String.valueOf(dia);
		}
		
		//Hora
		String strHora = "";
		if(hora < 10){
			strHora = "0" + hora;
		}
		else{
			strHora = String.valueOf(hora);
		}
		
		//Minutos
		String strMin = "";
		if(min < 10){
			strMin = "0" + min;
		}
		else{
			strMin = String.valueOf(min);
		}
		
		//Segundos
		String strSeg = "";
		if(seg < 10){
			strSeg = "0" + seg;
		}
		else{
			strSeg = String.valueOf(seg);
		}
		
		String fecha = "";
		fecha = anio + mesHoy + strDia + strHora + strMin + strSeg;
		
		return fecha;
	}
	/**
	 * Método que genera un zip con una serie de ficheros y los deja en un directorio dado
	 * @param pathOrigen - Path base donde se encuentran los ficheros origen acabado en "/"
	 * @param nombreFicherosAInsertar - Lista con los path relativos de los ficheros a incluir en el zip comenzando sin "/"
	 * @param pathDestino - path completo donde se dejará el zip generado acabado en "/"
	 * @param nombreZip - Nombre del fichero zip
	 * @return la ruta donde se encuentra el fichero ZIP o en caso de que haya algún error lo devolvemos vacío.
	 * @throws Exception
	 */
	public static String generarZip(
			String pathOrigen,
			List nombreFicherosAInsertar,
			String pathDestino,
			String nombreZip) throws FileNotFoundException, Exception {

		String outFilename = "";
		int numXML = 0;
		
		try {
			outFilename = pathDestino + "/" + nombreZip; // nombre del zip generado
			logger.debug("nombreZIP: " + outFilename);
			FileInputStream in = null;
			byte[] buf = new byte[1024];
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));

    	    // recorre los nombres de los ficheros para insertarlos en el zip
			String nombreFicheroOrigen;
            for(int i =0; i < nombreFicherosAInsertar.size(); i++){
            	nombreFicheroOrigen = "";
        	    if (!nombreFicherosAInsertar.get(i).equals("")){
        	    	nombreFicheroOrigen = nombreFicherosAInsertar.get(i).toString();
        	    	// Si en la ruta relativa no viene sólo el nombre del fichero
        	    	if (nombreFicheroOrigen.indexOf("\\") != -1) {
        	    		nombreFicheroOrigen = nombreFicheroOrigen.substring(nombreFicheroOrigen.lastIndexOf("\\") + 1);
        	    	}
        	    	
        	    	// Ruta del fichero a insertar
                    String path = pathOrigen + "/" + nombreFicheroOrigen;
                    //logger.debug("FICHERO ORIGEN:" + path);
                    // Creo el fichero destino
                    String pathTmp = pathDestino + "/" + nombreFicheroOrigen;
                    File f = new File(pathTmp);
                    //logger.debug("FICHERO DESTINO:" + pathTmp);
                    // Copio el original en el destino
                    try {
                    	copyFile(new File(path), f);
                    	
                        // Lo añado al zip
                        in = new FileInputStream(pathTmp);
                        out.putNextEntry(new ZipEntry(nombreFicheroOrigen));
                        int len;
                        while ((len = in.read(buf)) > 0) {
                    	    out.write(buf, 0, len);
                        }
                        
                        //cierra la escritura de la entrada del fichero
                        out.closeEntry();
                        in.close();
                        
                        numXML++;
                    }
                    catch (FileNotFoundException fnfe){
						logger.error("Fichero " + nombreFicheroOrigen + " no encontrado.");
					}
                    
                    //borra el fichero descargado de local
                    f.delete();
        	    }
            }
            // cierra la escritura del zip
            out.close();

		} 
		catch (Exception e) {
			logger.error("Error al generar el fichero ZIP: " + e.getMessage());
			numXML = 0;
		}

		return numXML + "";				
	}
	/**
	 * Método que copia un fichero
	 * @param origen
	 * @param destino
	 * @return boolean true or false según que se copie correctamente o no.
	 */
	public static boolean copyFile (
			File origen, 
			File destino) throws Exception { 
		boolean blnResultado = false;
		try {
			InputStream in = new FileInputStream(origen);
			OutputStream out = new FileOutputStream(destino);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			blnResultado = true;
		}
		catch (Exception ex) {
			logger.error("Error al copiar en local fichero:" + ex);
			ex.getMessage();
			ex.printStackTrace();
			throw ex;
		}
		return blnResultado;
	}
	/**
	 * Funcion para rellenar por la derecha, a un texto, espacios en blanco hasta alcanzar una longitud minima
	 * Si el texto sobre pasa esta longitud no se realiza ninguna accion
	 * 
	 * @param texto
	 * @param longitudMinima
	 * @return
	 */
	public static String AjustarTextos(String texto, int longitudMinima){
		//Definicion de Varibles
		String textoSalida = "";
		
		if ( (texto == null) || (texto.trim().equals("") ) ){
			//Si no esta informado o esta vacio devolvemos vacio
			textoSalida = "";
		}else{
			//
			if ( (longitudMinima != 0) && (texto.trim().length() < longitudMinima) ){
				//
				textoSalida = texto;
				for (int i = 0; i < (longitudMinima - texto.trim().length()); i++){
					textoSalida += " ";
				}
				
			}
			else {
				//Si la longitud minima es cero o menor que cero, devolvemos el texto exactamente igual a como entro
				textoSalida = texto;
			}
		}
		
		//Devolvemos el valor
		return textoSalida;
	}
}