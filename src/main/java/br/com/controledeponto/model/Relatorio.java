package br.com.controledeponto.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Relatorio {

	// Exibido em formato 2018-08.
	private String mes;

	// Exibido em formato PT69H35M5S.
	private String horasTrabalhadas;

	// Exibido em formato PT25M5S.
	private String horasExcedentes;

	// Exibido em formato PT0S.
	private String horasDevidas;

	private List<Registro> registros;

	private List<Alocacao> alocacoes;

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getHorasTrabalhadas() {
		return horasTrabalhadas;
	}

	public void setHorasTrabalhadas(String horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
	}

	public String getHorasExcedentes() {
		return horasExcedentes;
	}

	public void setHorasExcedentes(String horasExcedentes) {
		this.horasExcedentes = horasExcedentes;
	}

	public String getHorasDevidas() {
		return horasDevidas;
	}

	public void setHorasDevidas(String horasDevidas) {
		this.horasDevidas = horasDevidas;
	}

	public List<Registro> getRegistros() {
		return registros;
	}

	public void setRegistros(List<Registro> registros) {
		this.registros = registros;
	}

	public List<Alocacao> getAlocacoes() {
		return alocacoes;
	}

	public void setAlocacoes(List<Alocacao> alocacoes) {
		this.alocacoes = alocacoes;
	}

}
