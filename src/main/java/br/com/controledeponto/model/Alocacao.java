package br.com.controledeponto.model;

import org.springframework.stereotype.Component;

@Component
public class Alocacao {

	private String dia;
	private String tempo;
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

	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getTempo() {
		return tempo;
	}

	public void setTempo(String tempo) {
		this.tempo = tempo;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto) {
		this.nomeProjeto = nomeProjeto;
	}

}
