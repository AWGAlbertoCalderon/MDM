package com.viesgo.componentes;

import java.sql.Clob;

public class RecuperarConsultaClob_DTO {
	
	private Clob Consulta;

	public Clob getConsulta() {
		return Consulta;
	}

	public void setConsulta(Clob consulta) {
		Consulta = consulta;
	}

	public RecuperarConsultaClob_DTO(Clob consulta) {
		super();
		this.Consulta = consulta;
	}

	public RecuperarConsultaClob_DTO() {
		super();
		this.Consulta = null;

	}

	
}