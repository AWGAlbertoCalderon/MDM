package com.viesgo.componentes;

public class RecuperarConsulta_DTO {
	
	private String Consulta;

	public String getConsulta() {
		return Consulta;
	}

	public void setConsulta(String consulta) {
		Consulta = consulta;
	}

	public RecuperarConsulta_DTO(String consulta) {
		super();
		this.Consulta = consulta;
	}

	public RecuperarConsulta_DTO() {
		super();
		this.Consulta = null;

	}

	
}