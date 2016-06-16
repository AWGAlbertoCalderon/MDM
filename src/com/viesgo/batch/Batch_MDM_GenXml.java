package com.viesgo.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import com.ibatis.common.jdbc.SimpleDataSource;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.viesgo.componentes.MDM_DTO;
import com.viesgo.componentes.RecuperarConsultaClob_DTO;
import com.viesgo.utilidades.LogProceso;
import com.viesgo.utilidades.PropiedadesAplicacion;
import com.viesgo.utilidades.TratarFicheros;

public class Batch_MDM_GenXml {
	// Código de retorno para Control-M
	private static int controlM_ExitCode;
	
	private static boolean todoOK;
	
	private static String nombreServicio= "PROCESO_BATCH_MDM_GENXML";
	
	// Contadores para el resumen
	private static int debenMDM;
	private static String directorioProcesado;

	private static SqlMapClient sqlMap;

	private static Log logger = LogFactory.getFactory().getInstance(Batch_MDM_GenXml.class);
		
	Calendar now = Calendar.getInstance();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	private String fechaDia = formatter.format(now.getTime());
	
	public Batch_MDM_GenXml(String nombreFichero) {
    	
		try {
			directorioProcesado = PropiedadesAplicacion.getInstance().getPropiedad("DIRECTORIOPROCESADO");
		} catch (Exception e) {
			logger.error("ERROR Cargando properties " + e.getMessage());
		}
	}

	public static void main(String[] args) {

		controlM_ExitCode = 0;
    	BasicConfigurator.configure();
		
		try {
			logger.info("Inicio proceso GENERACION DE XML (MDM)");
	
			if (args.length < 3) {
				// Número de argumentos incorrectos
				logger.error("Es necesario incluir el nombre del fichero de configuracion como parametro");
				throw new Exception("NO EXISTEN FICHEROS DE PROPIEDADES PASADOS COMO PARAMETROS");
			}
			if(TratarFicheros.existeFichero(args[1])) {
				// Cambiamos el fichero de propiedades del log
				PropertyConfigurator.configure(args[1]);
			} else {
				logger.error("No se encuentra el fichero de propiedades de log");
			}

			sqlMap = PropiedadesAplicacion.LeerFicherosProperties(args[0], args[1], args[2]);
						
			try {
				todoOK = false; 

				LogProceso.Cabecera(nombreServicio);
				
				Batch_MDM_GenXml genXml = new Batch_MDM_GenXml(args[0]);
				
					List<MDM_DTO> datosNNSS = genXml.recuperaDatosMDM();
					todoOK = true;

				logger.info("FIN DEL PROCESO de GENERACION DE XMLs.");
	            
	    	} catch (Exception e) {
	    		logger.error("Error al generar XMLs:" + e.getMessage());
	    		controlM_ExitCode = 1;
	    	}
		}
		catch (Exception e){
			logger.error("Error al cargar los ficheros de propiedades motor:" + e.getMessage());
			controlM_ExitCode = 1;
	    }
		finally {
			//Escribimos la fecha de la ejecucion correcta
//			try	{
//				if (sqlMap != null) {
//					Date fechaHoy = new Date();
//					SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//					String fechaejecucion = formato.format(fechaHoy);
//					EjecucionesServicios.setEjecucion(sqlMap, "Batch_MDM_GenXml", fechaejecucion, PropiedadesAplicacion.getRutaLog(), todoOK, "MDM");
//				}
//			} catch (Exception ex) {
//				logger.error("No se ha guardado la fecha de ejecucion en la base de datos: " + ex.getMessage());
//				controlM_ExitCode = 1;
//		    }
			
			//Escribimos el resumen del proceso
			 EscribirResultado();
			
			//Escribimos el pie que muestra el tiempo que ha tardado el proceso
			LogProceso.Pie();
	    	logger.info("FINALIZACION DEL PROCESO DE GENERACION DE XMLs (MDM)");
	    	
			//Cerramos todas las conexiones del pool
	    	if (sqlMap != null) {
				SimpleDataSource ds = (SimpleDataSource) sqlMap.getDataSource();
		        ds.forceCloseAll();
			}
	    	System.exit(controlM_ExitCode);
	    }
	}			

	//Método que recupera los datos en BBDD.
	private List<MDM_DTO> recuperaDatosMDM() {

		logger.info("COMIENZA LA RECUPERACION DE DATOS A GENERAR XMLs ...");
		
		boolean bFicheroXML = true;
		
		List<MDM_DTO> datos = null;
		//List<RecuperarConsulta_DTO> consulta = null;
		List<RecuperarConsultaClob_DTO> consulta = null;
		try {
			
			consulta = sqlMap.queryForList("recuperarConsulta", 2);
						
			if (consulta.size() > 0) {
			
				//logger.debug(consulta.get(0).getConsulta());
								
				datos = sqlMap.queryForList("recuperaDatosMDM", consulta.get(0));
				
				debenMDM = datos.size();
	
				if (datos.size() > 0) {
					// Recorremos el listado y recuperamos el documento xml
					ListIterator<MDM_DTO> listado = datos.listIterator();
					while (listado.hasNext()) {
	
						MDM_DTO datosMDM = listado.next();
						// Pasamos la ruta y el nombre del documento
						if (datosMDM.getXMLNombreFichero() != null) {
							try {
								//Comprobamos si existe la carpeta con la fecha del fichero
								boolean bDirectorioFechaFichero = TratarFicheros.comprobarExisteDirectorio(directorioProcesado + File.separator + fechaDia);
	
								// si no existe la carpeta crearlas
								if (!bDirectorioFechaFichero) {
									File diaDirectorioProcesado = new File(directorioProcesado + File.separator + fechaDia);
									if (diaDirectorioProcesado.mkdirs()) {
										if (logger.isDebugEnabled()) {
											logger.debug("Se ha creado el directorio para el procesado " + directorioProcesado + File.separator + fechaDia + File.separator);
										}
									}
								}
	
								bFicheroXML = procesaDatosMDM(directorioProcesado + File.separator + fechaDia + File.separator, datosMDM.getXMLNombreFichero(), datosMDM);
	
							} catch (Exception e) {
								logger.error("Error al obtenera la carpeta " + directorioProcesado + fechaDia + ". Error: " + e.getMessage());
							}
						} else {
							logger.error("El IDoc " + datosMDM.getXMLNombreFichero() + " es nulo.");
						}
					}
				
				} else {
					logger.info("NO EXISTEN DATOS PARA GENERAR XMLs ...");
				}
			}else {
				logger.info("NO EXISTE CONSULTA A REALIZAR ...");
			}
	
		} catch (SQLException e) {
			logger.error("Error al recuperar datos validos: " + e.getMessage());
		}
		logger.info("FIN DE LA RECUPERACION DE DATOS.");
		return datos;
	}

	
	private boolean procesaDatosMDM(String ruta, String nombreFichero, MDM_DTO mdmDto) {

		boolean bCorrecto = true;
		
		try {
			boolean correcto = false;
			
			logger.debug("Creamos el " + ruta + nombreFichero);
			
			File ficheroACrear = new File(ruta + nombreFichero);
			
			//ESCRIBIR EL FICHERO
			PrintWriter wr = null;
			BufferedWriter bw = null;
			try{ 
				FileWriter w = new FileWriter(ficheroACrear); 
				bw = new BufferedWriter(w); 
				wr = new PrintWriter(bw); 
				wr.write(mdmDto.getXMLContenido());
				wr.close();
				bw.close();
				correcto=true;
			} catch(IOException e) {
				logger.error("Error " + e.getMessage());
				correcto=false;
			} 
			
		}catch(Exception e) {
			logger.error("Error " + e.getMessage());
		}
		return bCorrecto;
	}
//
////	//Convertir clob a string
//	private String clobToString(Clob data) {
//		StringBuilder sb = new StringBuilder();
//		try {
//			Reader reader = data.getCharacterStream();
//			BufferedReader br = new BufferedReader(reader);
//
//			String line;
//			while(null != (line = br.readLine())) {
//				sb.append(line);
//			}
//			br.close();
//		} catch (SQLException e) {
//			// handle this exception
//		} catch (IOException e) {
//			// handle this exception
//		}
//    return sb.toString();
//}

	public String openFileToString(byte[] _bytes)
	{
	    String file_string = "";

	    for(int i = 0; i < _bytes.length; i++)
	    {
	        file_string += (char)_bytes[i];
	    }

	    return file_string;    
	}

	
	/**
	 * Funcion que escribe el resumen del proceso en el fichero de log
	 */
	private static void EscribirResultado(){
		LogProceso.mensaje(" ");
		LogProceso.mensaje("PROCESO GENERACION DE XMLS:");
		LogProceso.mensaje("  -----------------------------------------------  ");
		LogProceso.mensaje(" ");
		
		LogProceso.registro("Ficheros XML a generar: ", debenMDM);


		LogProceso.mensaje(" ");
	}
}