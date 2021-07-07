package br.com.controledeponto.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Alocacao {

	@NotBlank
	@NotNull
	private String dia;
	@NotBlank
	@NotNull
	private String tempo;
	@NotBlank
	@NotNull
	private String nomeProjeto;

	public Alocacao() {
	}

	public Alocacao(String dia, String tempo, String nomeProjeto) {
		this.dia = dia;
		this.tempo = tempo;
		this.nomeProjeto = nomeProjeto;
	}

	public String getDia() {
		return dia;
	}

	public String getTempo() {
		return tempo;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

}
