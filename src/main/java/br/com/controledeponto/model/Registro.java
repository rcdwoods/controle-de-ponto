package br.com.controledeponto.model;

import java.util.List;

public class Registro {

	private String dia;
	private List<String> horarios;

	public Registro(String dia, List<String> horarios) {
		this.dia = dia;
		this.horarios = horarios;
	}

	public String getDia() {
		return this.dia;
	}

	public List<String> getHorarios() {
		return this.horarios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
		result = prime * result + ((horarios == null) ? 0 : horarios.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Registro other = (Registro) obj;
		if (dia == null) {
			if (other.dia != null)
				return false;
		} else if (!dia.equals(other.dia))
			return false;
		if (horarios == null) {
			if (other.horarios != null)
				return false;
		} else if (!horarios.equals(other.horarios))
			return false;
		return true;
	}

}
