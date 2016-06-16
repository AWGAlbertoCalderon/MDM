package com.viesgo.componentes;

public class MDM_DTO {
	private String Version;
	private String XMLContenido;
	private String XMLNombreFichero;

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String getXMLContenido() {
		return XMLContenido;
	}

	public void setXMLContenido(String xMLContenido) {
		XMLContenido = xMLContenido;
	}

	public String getXMLNombreFichero() {
		return XMLNombreFichero;
	}

	public void setXMLNombreFichero(String xMLNombreFichero) {
		XMLNombreFichero = xMLNombreFichero;
	}

	public MDM_DTO(
		String version, String xmlcontenido, String xmlnombrefichero) {
		super();
		this.Version = version;
		this.XMLContenido = xmlcontenido;
		this.XMLNombreFichero = xmlnombrefichero;	
	}

	public MDM_DTO() {
		super();
		this.Version = null;
		this.XMLContenido = null;
		this.XMLNombreFichero = null;
	}
}