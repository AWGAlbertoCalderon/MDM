/*
 * PropiedadesAplicacion.java
 */

package com.viesgo.utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

/**
 * Clase que almacena las propiedades de la aplicación que habran sido cargadas
 * desde un fichero de propiedades.
 */
public class PropiedadesAplicacion {
	
	private static Log logger = LogFactory.getFactory().getInstance(PropiedadesAplicacion.class);
	
	//Ruta logs web
	private static String rutaLog;
	
	//Acceso a BD
	private static String nroIntentosBD;
	private static String sleepBD;
	private static boolean bAcceso = false;
	
    public static String getRutaLog() {
		return rutaLog;
	}
    
	/**
     * Instancia de la clase PropiedadesAplicacion.
     * Utilizada para realizar la implementacion del patron singleton.
     */
    private static PropiedadesAplicacion instance = null; // Instancia única.
    /**
     * Atributo en el que se guardan las propiedades.
     */

    private static Properties propiedades = null; // Atributo interno en el que se guardan las propiedades.

    /**
     * Constructor de la clase.
     * El constructor es privado para no generarse más de un objeto (ver método getInstance).
     * Inicializa el objeto PropiedadesAplicacion, recogiendo un objeto Properties que almacenará en un atributo privado.
     * @param prop Objeto Properties a almacenar.
     */
    private PropiedadesAplicacion(Properties prop) {
    	propiedades = prop;
    }

    /**
     * Cosntructor de la clase
     * El constructor es privado para no generarse más de un objeto (ver método getInstance).
     * Inicializa el objeto PropiedadesAplicacion vacío.
     */
    private PropiedadesAplicacion() {
    }

    /**
     * Éste método se debe usar para obtener un objeto PropiedadesAplicacion.
     * Está sincronizado y es estático para no sobreescribir atributos.
     * @param prop Objeto Properties a almacenar.
     * @return Instancia PropiedadesAplicacion.
     */
    public static synchronized PropiedadesAplicacion getInstance(Properties prop) throws Exception {
    	if (instance == null) {
            instance = new PropiedadesAplicacion(prop);
        }
	return instance;
    }

    /**
     * Éste método se debe usar para obtener un objeto PropiedadesAplicacion.
     * Está sincronizado y es estático para no sobreescribir atributos.
     * Esta forma de obtener un objeto PropiedadesAplicacion está pensada para
     * cuando ya se ha creado una instancia con los orígenes de datos, para no
     * tener que introducir un parámetro.
     * @return Instancia PropiedadesAplicacion.
     */
    public static synchronized PropiedadesAplicacion getInstance() throws Exception {
        if (instance == null) {
            instance = new PropiedadesAplicacion();
        }
        return instance;
    }

   /**
     * Devuelve la propiedad cargada en el atributo privado relacionada con la
     * clave introducida como parámetro de entrada.
     * @param strProp Clave en la que introducir el valor.
     * @return Objeto obtenido del atributo privado (será necesario realizar una conversión).
     */
    public String getPropiedad(String strProp) {
    	return (propiedades.getProperty(strProp,""));
    }

    /**
     * Establece una nueva propiedad cargada en el atributo privado con los
     * parámetros de entrada.
     * @param strProp Clave en la que introducir el valor.
     * @param strValor Valor a introducir en el atributo privado.
     * @return Objeto obtenido del atributo privado (será necesario realizar una conversión).
     */
    public void setPropiedad(String strProp, String strValor) {
    	propiedades.setProperty(strProp,strValor);
    }
	
    /**
	 * Funcion que devuelve el valor de una propiedad
	 * @param props: Variable de tipo properties que contiene las propiedades
	 * @param parametro: String con el nombre de la propiedad
	 * @return String con el valor de la propiedad
	 */
    private static String getPropiedad(Properties props, String parametro){
		String resultado = null;
		if (props != null){
			if (props.get(parametro) != null && !props.get(parametro).equals("")) {
				resultado = (String) props.get(parametro);
			}
		}else{
			logger.warn("Batch_Validador.getPropiedad: El fichero de propiedades es nulo y el parametro solicitado es " + parametro);
		}
		
		return resultado;
	}

	/**
	 * Funcion que lee el fichero de configuracion y el sqlmap
	 * @param ficheroPropiedades: Ruta al fichero de propiedades
	 * @param ficheroLog4j:Ruta al fichero log4j
	 */    
	public static SqlMapClient LeerFicherosProperties(String ficheroPropiedades, String ficheroLog4j, String rutaSqlMap) throws Exception, IOException {
    	
		//Leemos el fichero de propiedades
		logger.info("Inicio de LeerFicherosProperties");
		logger.info("ficheroPropiedades = " + ficheroPropiedades);
		SqlMapClient sqlMap = null;
		Reader reader = null;
		

		try {
			CargarPropiedades.cargar(ficheroPropiedades);
	    	
	    	nroIntentosBD = PropiedadesAplicacion.getInstance().getPropiedad("nroIntentosBD").equals("") ? "5":PropiedadesAplicacion.getInstance().getPropiedad("nroIntentosBD").trim();
			sleepBD = PropiedadesAplicacion.getInstance().getPropiedad("sleepBD").equals("") ? "60000":PropiedadesAplicacion.getInstance().getPropiedad("sleepBD").trim();
			
			logger.info("Fichero de propiedades leido correctamente");
		} catch (IOException ioe) {
			throw new IOException("Error al leer el fichero de propiedades. " + ioe.getMessage() );
		}
		
		logger.info("Despues de leer fichero de propiedades");
		
		//Leemos el fichero log4j
		try{
			//Quitamos la ruta absoluta del log4j
			Properties propLog = new Properties();
			propLog = new Properties();
			
			File fichero = new File(ficheroLog4j);
			InputStream is_fichero_log = new FileInputStream(fichero); 
			propLog.load(is_fichero_log);
			
			String ruta = getPropiedad(propLog, "ruta_log_http");
			String prefijo = getPropiedad(propLog, "log4j.appender.logProceso.Prefix");
			String sufijo = getPropiedad(propLog, "log4j.appender.logProceso.Suffix");
			Date fechaHoy = new Date();
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			String fecha = formato.format(fechaHoy);
			rutaLog = ruta + prefijo + fecha + sufijo;
			
			logger.info("Ruta de log correcta");
			
		}catch (IOException ioe){
			throw new IOException("Error al leer el fichero log4j. " + ioe.getMessage() );
		}
		logger.info("Despues de la ruta de log");
		
		//Leemos el fichero donde se encuentran las sql
		InputStream is_fichero_Map = null; 
		int iContador = 0;
		
		//Se intenta conectar un nroIntentosBD a BD...
		while (!bAcceso) {
		
			iContador++;
			
			try{
				File fichero = new File(rutaSqlMap);
				is_fichero_Map = new FileInputStream(fichero);
				reader = (Reader) new InputStreamReader(is_fichero_Map);
				sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);

				sqlMap.queryForObject("VALIDATION_QUERY", null);
				
				logger.info("sqlMap correcto");
				
				bAcceso = true;
			}
			catch (IOException ioe){
				logger.error("Error al leer el fichero SqlMap. Intento: " + iContador);
				if (iContador >= Integer.parseInt(nroIntentosBD))
					throw new IOException("Error al leer el fichero SqlMap. Mensaje = " + ioe.getMessage() );
			}catch (SQLException ioe){
				logger.error("Error SQL al leer el fichero SqlMap. Intento: " + iContador);
				if (iContador >= Integer.parseInt(nroIntentosBD))
					throw new SQLException("Error SQL al leer el fichero SqlMap. Mensaje = " + ioe.getMessage() );
			}
			catch (Exception e){
				logger.error("Error al chequear el fichero SqlMap. Intento: " + iContador);
				if (iContador >= Integer.parseInt(nroIntentosBD))
					throw new Exception("Error al chequear el fichero SqlMap. Mensaje = " + e.getMessage() );
			} finally {
				try {
					if (reader != null) reader.close();
					if (is_fichero_Map != null) is_fichero_Map.close();
				}catch (Exception e){
					logger.error("Error cerrando recursos: " + e.getMessage());
				}	
			}
			
			if (!bAcceso){
				logger.info("Espera " + sleepBD + " ms...");
				Thread.sleep(Long.parseLong(sleepBD));
			}	
		}
		
		logger.info("Fin LeerFicherosProperties");
		
		return sqlMap;
	}	
}