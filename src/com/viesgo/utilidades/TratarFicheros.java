package com.viesgo.utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPTransferType;

public class TratarFicheros {
	
	private static Log logger = LogFactory.getFactory().getInstance(TratarFicheros.class);
	private FTPClient ftp = null;
	
	public TratarFicheros() {
	}
	
	public TratarFicheros(String ftpFlag) {
		ftp = new FTPClient();
	}

	/**
	 * Función para comprobar si existe un fichero dado
	 */
	public static boolean existeFichero(String fichero) {
		boolean existe = false;
		File file = new File(fichero);
	    if(file.exists()) {
	    	existe = true;
	    }
		return existe;
	}	

	/**
	 * Método que copia un fichero con renameTo()
	 * @param origen
	 * @param destino
	 * @return boolean true or false según que se copie correctamente o no.
	 */
	public static void moverFicheros(String oldName, String newName) throws IOException {
	    File srcFile = new File(oldName);
	    boolean bSucceeded = false;
	    try {
	        File destFile = new File(newName);
	        if (destFile.exists()) {
	            if (!destFile.delete()) {
	                throw new IOException(oldName + " was not successfully renamed to " + newName); 
	            }
	        }
	        if (!srcFile.renameTo(destFile))        {
	            throw new IOException(oldName + " was not successfully renamed to " + newName);
	        } else {
	                bSucceeded = true;
	        }
	    } finally {
	          if (bSucceeded) {
	                srcFile.delete();
	          }
	    }
	}

	/**
	 * Método que copia un fichero con renameTo() sin comprobacion de existe destino ni asegurado de borrado origen
	 * @param origen
	 * @param destino
	 * @return boolean true or false según que se copie correctamente o no.
	 */
	public static void moverFicherosSimple(String oldName, String newName) throws IOException {
	    File srcFile = new File(oldName);
	    File destFile = new File(newName);

	    if (!srcFile.renameTo(destFile)) {
	    	if (!destFile.delete()) {
                throw new IOException(oldName + " no he conseguido borrar en destino " + newName); 
            }
	    	if (!srcFile.renameTo(destFile))        {
	            throw new IOException(oldName + " no he conseguido renombrar a destino " + newName);
	        } 
	    }
	}
	
	public static boolean deleteFile (String fichero) {
		boolean blnResultado = false;
		
		try {
			File f = new File(fichero);
			f.delete();
			
			blnResultado = true;
		}
		catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return blnResultado;
	}

	public boolean deleteCarpeta (String directorio, String carpeta) {
		
		boolean bCtrl = false;

		try {
			//vamos al directorio del FTP
			if(!directorio.equals("")) {
				// Inicialmente voy al directorio inicial de la conexión.
				ftp.chdir("/");
				// Me muevo al directorio destino
				try {
					ftp.chdir(directorio);
				} catch (Exception e) {
					logger.error("El directorio " + directorio + " no existe.");
				}			
				// Borro la carpeta.
				try {
					ftp.rmdir(carpeta);
				} catch (Exception e) {
					logger.error("No se ha podido eliminar la carpeta " + carpeta + ":" + e);
				}
				bCtrl = true;
			}
		} catch (Exception e) {
			logger.error("Error en la eliminación de la carpeta: " + e.getMessage());
		}
		
		return bCtrl;
	}

	public boolean abrirConnFTP(
			String ipFTP, 
			String Usuario_FTP, 
			String Password_FTP			
			) {
		boolean bCtrl = false;
	
		try {
			
			// set up client    
			ftp.setRemoteHost(ipFTP);
			
			// connect
			ftp.connect();
			
			// login
			ftp.login(Usuario_FTP, Password_FTP);

			// set up passive ASCII transfers
			ftp.setConnectMode(FTPConnectMode.PASV);
			ftp.setType(FTPTransferType.ASCII);

			bCtrl = true;
		} 
		catch (Exception e) {
			logger.error("Error al abrir la conexion FTP: " + e.getMessage());
		}

		return bCtrl;		
	}
	
	public boolean cerrarConnFTP() {
	
		boolean bCtrl = false;
		
		try {
			// Shut down client                
			ftp.quit();

			bCtrl = true;
		} 
		catch (Exception e) {
			logger.error("Error en la subida del fichero: " + e.getMessage());
			try{
				if(ftp.connected()) ftp.quit();
			}
			catch(Exception ftpExc){
				logger.error("Error al cerrar la conexion ftp: " + ftpExc.getMessage());
			}
		}

		return bCtrl;		
	}
	
	/**
	 * 
	 * @param pathFicheroOrigen - Ruta completa del fichero a copiar 
	 * @param directorioDestino - Directorio destino en el FTP en la forma: /dir1/dir2/.../dirn
	 * @param ficheroDestino - Nombre del Fichero destino.
	 * @return
	 */
	public boolean enviarFTP(
				String pathFicheroOrigen,
				String directorioDestino, 
				String ficheroDestino ) {
		
		logger.info("Enviando por FTP --> "+ficheroDestino);

		boolean bEnvioFTP = false;
		
		try {
			//vamos al directorio del FTP
			if(!directorioDestino.equals("")) {
				String[] directorios = directorioDestino.split("/");
				
				// Inicialmente voy al directorio inicial de la conexión.
				ftp.chdir("/");
				// Recorro toda la estructura de directorios y  si alguno no existe lo creo.
                for (int i = 1; i <= directorios.length-1 ; i++) {
    				try {
    					ftp.chdir(directorios[i]);
    				} catch (Exception e) {
    					logger.info("El directorio " + directorios[i] + " no existe. Se crea.");
    					//Se crea el directorio
    					ftp.mkdir(directorios[i]);
    					//Se accede al directorio
    					ftp.chdir(directorios[i]);
    				}
            	}
			}

			// copy file to server
			ftp.put(pathFicheroOrigen, ficheroDestino);

			bEnvioFTP = true;
		} catch (Exception e) {
			logger.error("Error en la subida del fichero: " + e.getMessage());
		}

		return bEnvioFTP;
	}	
	
	public boolean isConnAlive () {
		logger.debug("ALIVE");
		return ftp.connected();
	}
		
	public static String recuperarFecha() {
		//Recuperamos la fecha y hora actual (yyyymmddhhmmss)
		// Recuperamos fecha
		Calendar myCalendar = Calendar.getInstance();
		
		String fechaCompleta = "";
		
		int dia = myCalendar.get(Calendar.DAY_OF_MONTH);
		int mes = myCalendar.get(Calendar.MONTH);
		if (mes == 0) {
			mes = 1;
		} else {
			mes += 1;
		}
		int anio = myCalendar.get(Calendar.YEAR);
		int hora = myCalendar.get(Calendar.HOUR_OF_DAY);
		int minuto = myCalendar.get(Calendar.MINUTE);
		int segundo = myCalendar.get(Calendar.SECOND);
		
		fechaCompleta = anio + 
						CompletarFecha(String.valueOf(mes),2) +
						CompletarFecha(String.valueOf(dia),2) +
						CompletarFecha(String.valueOf(hora),2) +
						CompletarFecha(String.valueOf(minuto),2) +
						CompletarFecha(String.valueOf(segundo),2);
						
		
		return fechaCompleta;
	}

	/**
	 * Funcion para rellenar por la izquierda, a un texto, con ceros hasta alcanzar una longitud minima
	 * Si el texto sobre pasa esta longitud no se realiza ninguna acción
	 * 
	 * @param texto
	 * @param longitudMinima
	 * @return
	 */
	public static String CompletarFecha(String texto, int longitudMinima){
		//Definicion de Varibles
		String textoSalida = "";
		
		if ((texto == null) || (texto.trim().equals("") ) ){
			//Si no esta informado o esta vacio devolvemos vacio
			textoSalida = "";
		}else{
			//
			if ((longitudMinima != 0) && (texto.trim().length() < longitudMinima) ){
				//
				for (int i = 0; i < (longitudMinima - texto.trim().length()); i++){
					textoSalida += "0";
				}
				textoSalida += texto;
			} else {
				//Si la longitud minima es cero o menor que cero, devolvemos el texto exactamente igual a como entro
				textoSalida = texto;
			}
		}
		//Devolvemos el valor
		return textoSalida;
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
		/*
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
		*/
		
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

	public static boolean desplazar(File source, File destination) {
        if( !destination.exists() ) {
        	// intentamos con renameTo
            boolean result = source.renameTo(destination);
            if (!result) {
            	// intentamos copiar
                result = true;
                result &= copiar(source, destination);
                result &= source.delete();
            } return(result);
        } else {
            // Si el fichero destination existe, cancelamos...
            return(false);
        } 
	}
	
	/** copia el fichero source en el fichero destination
	 * devuelve true si funciona correctamente
	 */
	public static boolean copiar(File source, File destination)	{
		boolean resultado = false;
	    // declaración del flujo
	    FileInputStream sourceFile=null;
	    FileOutputStream destinationFile=null;
	    try {
	    	// creamos fichero
	        destination.createNewFile();
	        // abrimos flujo
	        sourceFile = new FileInputStream(source);
	        destinationFile = new FileOutputStream(destination);
	        // lectura por segmentos de 0.5Mb
	        byte buffer[]=new byte[512*1024];
	        int nbLectura;
	        while((nbLectura = sourceFile.read(buffer)) != -1) {
	        	destinationFile.write(buffer, 0, nbLectura);
	        } 
	        // copia correcta
	        resultado = true;
	     } catch(FileNotFoundException f) {
	     } catch(IOException e) {
	     } finally {
	     // pase lo que pase, cerramos flujo
	    	 try {
	    		 sourceFile.close();
	         } catch(Exception e) { }
	         try {
	        	 destinationFile.close();
	         } catch(Exception e) { }
	     } 
	     return(resultado);
	}
	
	public static boolean comprobarExisteDirectorio(String path) {
		boolean esDir=false;
		
		// crear el objeto file para obtener información sobre el
		if ( path != null && !"".equals(path)) {
			File fichero = new File(path);
			if ( fichero.exists() && fichero.isDirectory() ) {
				esDir = true;
			}
		}
		
		return esDir;
	}
	
	public static URL createURL(String s){
		
		URL url = null;
		try {
			url = new URL(s);
		} catch(MalformedURLException malformedurlexception){
			File file = new File(s);
	
			try {
					String s1 = file.getAbsolutePath();
					String s2 = System.getProperty("file.separator");
					if(s2.length() == 1){
					
						char c = s2.charAt(0);
						if(c != '/') s1 = s1.replace(c, '/');
						if(s1.charAt(0) != '/') s1 = '/' + s1;
					}
					s1 = "file://" + s1;
					url = new URL(s1);
			}catch(MalformedURLException malformedurlexception1){
				System.exit(0);
			}
		}
		return url;
	}
}