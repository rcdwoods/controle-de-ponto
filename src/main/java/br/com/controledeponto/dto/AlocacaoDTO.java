package br.com.controledeponto.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import br.com.controledeponto.model.Alocacao;

@Document
public class AlocacaoDTO {

	private String tempo;
	private String nomeProjeto;

	public AlocacaoDTO(Alocacao alocacao) {
		this.tempo = alocacao.getTempo();
		this.nomeProjeto = alocacao.getNomeProjeto();
	}

	public String getTempo() {
		return tempo;
	}

	public String getNomeProjeto() {
		return nomeProjeto;
	}

}
