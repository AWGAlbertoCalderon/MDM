package com.viesgo.utilidades;

//Importaciones
import org.apache.log4j.*;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * Clase para crear la cabecera, resumen de actuaciones y el pie en el fichero Log de los procesos batch
 * Permite su escritura de la cabecera y pie de forma reducida o completa
 * 
 * @author Mundivía S.A.
 * Fecha: 10/2006
 * Versión: 1.0
 * 
 */
public class LogProceso {
	//Definición de variables generales
	private static Logger loggerResultado = Logger.getLogger(LogProceso.class);
	//private static Logger loggerResultado ;
	private static Date fechaInicio = new Date();
	private static Date fechaFin = new Date();
	private static String Titulo_NombreProceso = "Nombre del proceso";
	private static String Titulo_DuracionProceso = "Duración proceso";
	private static String Titulo_FechaInicio = "Fecha de Inicio";
	private static String Titulo_FechaFin = "Fecha de Fin";
	private static int longitudMinimaTexto = 30;
	private static int longitudMinimaTextoEnvio = 2;
	/**
	 * Funcion que permite indicar un nombre de log para el fichero de propiedades del log4j
	 * 
	 * @param nombre
	 */
	public static void setNombreFicheroLog(String nombre){
		//Se verifica si el nombre esta informado
		if( (nombre != null) && (!nombre.trim().equals("")) ){
			//Se para el nombre del codigo y sera el fichero de propiedades el que defina el fichero de log
			loggerResultado = Logger.getLogger(nombre);
		}
		else{
			//Valor por defecto
			loggerResultado = Logger.getLogger(LogProceso.class);
		}
	}
	
	
	/**
	 * Escribe la cabecera del fichero de log especificando el nombre del fichero que debe ser utilizado según el fichero de propiedades del log4j
	 * 
	 * @param nombreProceso
	 * @param ficheroPropiedades
	 */
	public static void Cabecera(String nombreProceso, String identificadorProcesoLog){
		if( (identificadorProcesoLog != null) && (!identificadorProcesoLog.trim().equals("")) ){
			//Se define el fichero de log según el identificador el proceso
			setNombreFicheroLog(nombreProceso);
		}
		
		//Lamamos a la cabecera
		Cabecera(nombreProceso);
	}
	
	/**
	 * Escribe la cabecera reducida del fichero de log especificando el nombre del fichero que debe ser utilizado según el fichero de propiedades del log4j
	 * 
	 * @param nombreProceso
	 * @param ficheroPropiedades
	 */
	public static void CabeceraReducida(String nombreProceso, String identificadorProcesoLog){
		if( (identificadorProcesoLog != null) && (!identificadorProcesoLog.trim().equals("")) ){
			//Se define el fichero de log según el identificador el proceso
			setNombreFicheroLog(nombreProceso);
		}
		
		//Lamamos a la cabecera
		CabeceraReducida(nombreProceso);
	}
	
	/**
	 * Escribe la cabecera del fichero de log del proceso batch con el nombre y fecha de inicio
	 * 
	 * @param nombreProceso: Nombre identificativo del proceso que se ha ejecutado
	 */
	public static void Cabecera(String nombreProceso){
		//Definición de varibles
		String proceso = "";
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		if (nombreProceso != null){ proceso = nombreProceso.trim().toUpperCase(); }
		//Obtenemos la fecha de inicio
		fechaInicio = new Date();
		
		//Escribimos la cabecera del fichero de log que contiene el nombre del proceso y la fecha de inicio del mismo
		
		loggerResultado.info("********************************************************************************************************");
		loggerResultado.info("* " + Util.AjustarTextos(Titulo_NombreProceso,longitudMinimaTexto) + ": " + proceso);
		loggerResultado.info("* " + Util.AjustarTextos(Titulo_FechaInicio,longitudMinimaTexto) + ": " + formateador.format(fechaInicio));
		
		loggerResultado.info("* " + Util.AjustarTextos("Java version",longitudMinimaTexto) + ": " + System.getProperty("java.version") );
		loggerResultado.info("* " + Util.AjustarTextos("Sistema Operativo",longitudMinimaTexto) + ": " + System.getProperty("os.name"));
		
		loggerResultado.info("*------------------------------------------------------------------------------------------------------*");
		loggerResultado.info("");
	}
	
	public static Date CabeceraBIS(String nombreProceso){
		//Definición de varibles
		String proceso = "";
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		if (nombreProceso != null){ proceso = nombreProceso.trim().toUpperCase(); }
		//Obtenemos la fecha de inicio
		Date fechaInicioTmp = new Date();
		
		//Escribimos la cabecera del fichero de log que contiene el nombre del proceso y la fecha de inicio del mismo
		
		loggerResultado.info("********************************************************************************************************");
		loggerResultado.info("* " + Util.AjustarTextos(Titulo_NombreProceso,longitudMinimaTexto) + ": " + proceso);
		loggerResultado.info("* " + Util.AjustarTextos(Titulo_FechaInicio,longitudMinimaTexto) + ": " + formateador.format(fechaInicioTmp));
		loggerResultado.info("*------------------------------------------------------------------------------------------------------*");
		loggerResultado.info("");
		return fechaInicioTmp;
	}
	public static void DurProceso (Date fechaIni, Date fechaFin){
		//Definicion de variables
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		
		
		
		String Duracion = null;
		
		//Escribe el pie del fichero de log con la fecha fin del proceso y el tiempo que ha tardado en ejecutarse
		loggerResultado.info("");
		loggerResultado.info("*------------------------------------------------------------------------------------------------------*");
		loggerResultado.info("* " + Util.AjustarTextos(Titulo_FechaFin,longitudMinimaTexto) + ": " + formateador.format(fechaFin));
		loggerResultado.info("* " + Util.AjustarTextos(Titulo_DuracionProceso,longitudMinimaTexto) + "= " + getDuracionFecha(fechaIni, fechaFin) );
		loggerResultado.info("--------------------------------------------------------------------------------------------------------");
		loggerResultado.info("");
	
	
	}
	
	
	public static void DurEnvio (String procedencia){
	SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		
		
		Date fechaIni = null;
		String Duracion = null;
		Date fechaFin= null;
		//Escribe el pie del fichero de log con la fecha fin del proceso y el tiempo que ha tardado en ejecutarse
		loggerResultado.info("");
		loggerResultado.info("*------------------------------------------------------------------------------------------------------*");
		loggerResultado.info("* " + Util.AjustarTextos(Titulo_DuracionProceso,longitudMinimaTextoEnvio) + ": No se ha enviado el fichero generado porque todavía no se ha recibido el fichero de respuesta procedente del " + procedencia);
		loggerResultado.info("--------------------------------------------------------------------------------------------------------");
		loggerResultado.info("");
	}
	
	
	
	
	
	/**
	 * Escribe el pie del fichero de log del proceso batch con la fecha de fin y la duración en horas, minutos, segundos y milisegundos
	 *
	 */
	public static void Pie(){
		//Definicion de variables
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		fechaFin = new Date();
		
		//Escribe el pie del fichero de log con la fecha fin del proceso y el tiempo que ha tardado en ejecutarse
		loggerResultado.info("");
		loggerResultado.info("*------------------------------------------------------------------------------------------------------*");
		loggerResultado.info("* " + Util.AjustarTextos(Titulo_FechaFin,longitudMinimaTexto) + ": " + formateador.format(fechaFin));
		loggerResultado.info("* " + Util.AjustarTextos(Titulo_DuracionProceso,longitudMinimaTexto) + "= " + getDuracion() );
		loggerResultado.info("********************************************************************************************************");
		loggerResultado.info("");
	}
	
	/**
	 * Clase que permite escribir un texto y su valor. 
	 * Esta orientado a escribir el número de registros que se han leido, insertados, actualizados, .... en el proceso
	 * 
	 * @param nombre: Texto a mostrar
	 * @param valor: Valor del texto introducido
	 */
	public static void registro(String nombre, long valor){
		//Definicion de variables
		String nombreRegistro = "";
		long valorRegistro = 0;
		if (nombre != null){ nombreRegistro = nombre.toUpperCase(); }
		if (valor >= 0){ valorRegistro = valor; }
		
		//Escribe en el log los valores introducidos con un formato
		loggerResultado.info("    " + nombreRegistro + ": " + valorRegistro);
	}
	
	public static void registroString(String nombre, String valor){
		//Definicion de variables
		String nombreRegistro = "";
		String valorRegistro = "";
		if (nombre != null){ nombreRegistro = nombre.toUpperCase(); }
		if (valor != null){ valorRegistro = valor; }
		
		//Escribe en el log los valores introducidos con un formato
		loggerResultado.info("    " + nombreRegistro + ": " + valorRegistro);
	}
	
	
	/**
	 * Clase que permite escribir un texto y su valor que en esta caso es un String. 
	 * @param nombre: Texto a mostrar
	 * @param valor: texto introducido que es un String.
	 * 
	 */
	public static void resultado(String nombre, String valorS){
		//Definicion de variables
		String nombreRegistro = "";
		
		if (nombre != null){ nombreRegistro = nombre.toUpperCase(); }
		
		//Escribe en el log los valores introducidos con un formato
		loggerResultado.info("    " + nombreRegistro + ": " + valorS);
	}
	
	/**
	 * Metodo que permite escribir un texto en el fichero de log
	 * @param texto
	 */
	public static void mensaje(String texto){
		//Definicion de variables
		String textoAux = "";
		
		if (texto != null){
			textoAux = texto.trim();
		}
		
		//Escribe en el log el texto introducido
		loggerResultado.info(textoAux);
	}
	
	/**
	 * Clase que permite escribir un texto y su valor que en esta caso es un array. 
	 * @param nombre: Texto a mostrar
	 * @param valor: texto introducido que es un array de String.
	 */
	public static void resultadoArray(String nombre, List listaS){
		//Definicion de variables
		String nombreRegistro = "";
		int numEle = 0;
		String valor = "";
		int partes= 0;
		int resto= 0;
		int tamanioNombre = nombre.length();
		String vacio = "";
		
		//Se construye un string con tantos espacios como caracteres tiene el nombre
		for(int i=0; i<tamanioNombre; i++){
			vacio = vacio + " ";
		}
		
		if (nombre != null){ nombreRegistro = nombre.toUpperCase(); }
		
		if(listaS!=null){ numEle = listaS.size(); }
		
		//Se escribe en el log el registro aunque esté vacío.
		if(numEle==0){
			loggerResultado.info("    " + nombreRegistro + ": " + valor);
		}
		
		partes = numEle/10;
		resto = numEle%10;
		
		//Escribe en el log los valores introducidos con un formato
		for(int i=0; i<partes; i++){
			valor="";
			for(int j=0; j<10; j++){
	
				if(j==0){
					valor = (String) listaS.get(i*10 + j);
				}else{
					valor = valor + ","+ (String) listaS.get(i*10 + j);
				}
			}
			
			if(i==0){ 
				loggerResultado.info("    " + nombreRegistro + ": " + valor);
			}else{
				loggerResultado.info("    " + vacio + "  " + valor);
			}
		}
		
		if(resto>0){
			valor="";
			
			for(int z=0; z<resto; z++){
				if(z==0){
					valor = (String) listaS.get(partes*10 + z);
				}else{
					valor = valor + ","+ (String) listaS.get(partes*10 + z);
				}
			}
			
			if(partes>0){
				loggerResultado.info("    " + vacio + "  " + valor);
			}else{
				loggerResultado.info("    " + nombreRegistro + ": " + valor);
			}
							
		}

	}
	
	/**
	 * Escribe la cabecera reducida del fichero de log del proceso batch con el nombre y fecha de inicio
	 * 
	 * @param nombreProceso: Nombre identificativo del proceso que se ha ejecutado
	 */
	public static void CabeceraReducida(String nombreProceso){
		//Definición de varibles
		String proceso = "";
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		if (nombreProceso != null){ proceso = nombreProceso.trim().toUpperCase(); }
		//Obtenemos la fecha de inicio
		fechaInicio = new Date();
		
		//Escribimos la cabecera del fichero de log que contiene el nombre del proceso y la fecha de inicio del mismo
		loggerResultado.info("****************************************************************************************************");
		loggerResultado.info("* " + Titulo_NombreProceso + ": " + proceso + "  -- " + Titulo_FechaInicio + ": " + formateador.format(fechaInicio));
	}
	
	/**
	 * Escribe el pie reducido del fichero de log del proceso batch con la fecha de fin y la duración en horas, minutos, segundos y milisegundos
	 *
	 */
	public static void PieReducida(){
		//Definicion de variables
		SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		fechaFin = new Date();
		
		//Escribe el pie del fichero de log con la fecha fin del proceso y el tiempo que ha tardado en ejecutarse
		loggerResultado.info("* " + Titulo_FechaFin + ": " + formateador.format(fechaFin) + "  -- " + Titulo_DuracionProceso + " = " + getDuracion() );
		loggerResultado.info("****************************************************************************************************");
	}
	
	/**
	 * Clase para mostrar formateado el tiempo que ha tardado el proceso 
	 * 
	 * @return Texto con la duración del proceso, formateado en horas, minutos, segundos y milisegundos
	 */
	private static String getDuracion(){
		//Definición de variables
		String s_duracion = "";
		long l_duracion = fechaFin.getTime() - fechaInicio.getTime();

		//Recupera la duracion del proceso ya formateado
		s_duracion = Horas(l_duracion);
		
		return s_duracion;
	}
	private static String getDuracionFecha(Date fechaIni, Date fechaFin){
		//Definición de variables
		String s_duracion = "";
		long l_duracion = fechaFin.getTime() - fechaIni.getTime();

		//Recupera la duracion del proceso ya formateado
		s_duracion = Horas(l_duracion);
		
		return s_duracion;
	}

	
	/**
	 * Devuelve los segundos y milisegundos que ha tardado el proceso en ejecutarse. 
	 * Antes han sido tratados los minutos y horas
	 * 
	 * @param duracion: Tiempo que ha tardado el proceso en milisegundos
	 * @return Devuelde los segundos y milisegundos del proceso
	 */
	private static String Segundos(long duracion){
		//Definicion de variables
		long l_ms = 0;
		long l_seg = 0;
		String s_duracion = "";
		long segundosMilisegundo = 1000;
		
		//Separamos los segundos de los milisegundos
		l_ms = duracion % segundosMilisegundo;
		l_seg = (long)(duracion/segundosMilisegundo);

		//Formateamos la salida de los segundos y milisegundos
		s_duracion += l_seg + " s " + l_ms + " ms "; 
		
		return s_duracion;
	}
	
	/**
	 * Devuelve los minutos, segundos y milisegundos que ha tardado el proceso en ejecutarse. 
	 * Antes han sido tratados las y horas. Los segundos y milisegundos han sido tratados por la funcion Segundos(duracion)
	 * 
	 * @param duracion: Tiempo que ha tardado el proceso en milisegundos
	 * @return Devuelde los minutos, segundos y milisegundos del proceso 
	 */
	private static String Minutos(long duracion){
		//Definicion de variables
		long l_seg = 0;
		long l_min = 0;
		String s_duracion = "";
		long minutosMilisegundo = 60000;
		
		//Obtenemos los minutos y el resto corresponde a los segundos y milisegundos
		l_seg = duracion % minutosMilisegundo;
		l_min = (long)(duracion/minutosMilisegundo);

		//Formateamos la salida de los minutos, los segundos y milisegundos nos vienen formateados de la funcion Segundos(duracion)
		s_duracion += l_min + " m " + Segundos(l_seg);
		
		return s_duracion;
	}
	
	/**
	 * Devuelve las horas, minutos, segundos y milisegundos que ha tardado el proceso en ejecutarse. 
	 * Los minutos, segundos y milisegundos han sido tratados por la funcion Minutos(duracion)
	 * 
	 * @param duracion: Tiempo que ha tardado el proceso en milisegundos
	 * @return Devuelde las horas, minutos, segundos y milisegundos del proceso
	 */
	private static String Horas(long duracion){
		//Definicion de variables
		long l_min = 0;
		long l_hor = 0;
		String s_duracion = "";
		long horasMilisegundo = 3600000;
		
		//Obtenemos las horas y el resto corresponde a los minutos, segundos y milisegundos
		l_min = duracion % horasMilisegundo;
		l_hor = (long)(duracion/horasMilisegundo);
		
		//Formateamos la salida de las horas, los minutos, segundos y milisegundos nos vienen formateados de la funcion Minutos(duracion)
		s_duracion += l_hor + " h " + Minutos(l_min); 
		
		return s_duracion;
	}
}
