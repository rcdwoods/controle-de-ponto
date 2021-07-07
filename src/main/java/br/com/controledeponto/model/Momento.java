package br.com.controledeponto.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Document
public class Momento {

	@NotNull
	@NotBlank
	private String dataHora;
	
	public Momento() {
	}
	
	public Momento(String dataHora) {
		this.dataHora = dataHora;
	}

	public String getDataHora() {
		return dataHora;
	}

}
