package br.com.controledeponto.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document
public class Momento {

	// Exemplo: "2018-08-22T08:00:00"
	@NotNull
	@NotBlank
	private String dataHora;

	public String getDataHora() {
		return dataHora;
	}

	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}

}
