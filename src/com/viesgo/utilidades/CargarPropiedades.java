/*
 * CargarPropiedades.java
 *
 * Created on 19 de enero de 2005, 11:53
 */

package com.viesgo.utilidades;

import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.viesgo.utilidades.PropiedadesAplicacion;

public class CargarPropiedades {
    /**
     * Objeto utilizado para el tratamiento de logs.
     */
    private static Log logger = LogFactory.getLog(CargarPropiedades.class);
    
    /** Creates a new instance of CargarPropiedades */
    public CargarPropiedades() {        
    }
    
	public static void cargar(String nombreProperties) throws Exception{
	try {
		//Se cargan las propiedades de la aplicacion del fichero CSAMIRCOM
		ResourceBundle rsPropiedades = ResourceBundle.getBundle(nombreProperties);
		String strClave="";
		String strValor="";
		Properties objPropiedades = new Properties();
		Enumeration enumPropiedades = rsPropiedades.getKeys();
		  
		logger.debug("***********************************");
		logger.debug("CARGA PROPIEDADES DE LA APLICACION:");
		logger.debug("***********************************");
	  
		while (enumPropiedades.hasMoreElements()) {
			strClave = (String) enumPropiedades.nextElement();
			strValor=rsPropiedades.getString(strClave);
			objPropiedades.setProperty(strClave, strValor);
			logger.debug("PROPIEDAD " + strClave + ": " + strValor);
			}
		
		//inicializamos el objeto con los parametros obtenidos del fichero de properties
		PropiedadesAplicacion.getInstance(objPropiedades);
		logger.debug("***********************************");
		logger.info("CARGA OK PROPIEDADES FICHERO");
		} catch (Exception e){	
			logger.error("Carga KO de las propiedades del fichero, Exception: " + e);
			//Se lanza la excepción
		throw e;
		}
	}
}