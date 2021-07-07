package br.com.controledeponto.model;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.controledeponto.dto.AlocacaoDTO;

@Component
public class Relatorio {

	private String mes;

	private String horasTrabalhadas;

	private String horasExcedentes;

	private String horasDevidas;

	private List<Registro> registros;

	private List<AlocacaoDTO> alocacoes;

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getMes() {
		return mes;
	}

	public String getHorasTrabalhadas() {
		return horasTrabalhadas;
	}

	public String getHorasExcedentes() {
		return horasExcedentes;
	}

	public String getHorasDevidas() {
		return horasDevidas;
	}

	public List<Registro> getRegistros() {
		return registros;
	}

	public void setHorasTrabalhadas(String horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
	}

	public void setHorasExcedentes(String horasExcedentes) {
		this.horasExcedentes = horasExcedentes;
	}

	public void setHorasDevidas(String horasDevidas) {
		this.horasDevidas = horasDevidas;
	}

	public void setRegistros(List<Registro> registros) {
		this.registros = registros;
	}

	public void adicionarRegistro(Registro registro) {
		this.registros.add(registro);
	}

	public List<AlocacaoDTO> getAlocacoes() {
		return alocacoes;
	}

	public void setAlocacoes(List<AlocacaoDTO> alocacoes) {
		this.alocacoes = alocacoes;
	}

}
