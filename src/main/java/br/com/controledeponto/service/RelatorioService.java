package br.com.controledeponto.service;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.controledeponto.model.Registro;
import br.com.controledeponto.model.Relatorio;
import br.com.controledeponto.validator.ValidacaoDeDatas;

@Service
public class RelatorioService {
	
	private MomentoService momentoService;
	private RegistroService registroService;
	@Autowired
	private AlocacaoService alocacaoService;

	public RelatorioService(MomentoService momentoService, RegistroService registroService) {
		this.momentoService = momentoService;
		this.registroService = registroService;
	}
	
	public Relatorio getRelatorioDoMes(String mes) {
		validaFormatoDaDataInformada(mes);
		this.registroService.validaExistenciaDeRegistrosNoMes(mes);
		
		Relatorio relatorioGerado = new Relatorio();
		
		relatorioGerado.setMes(mes);
		relatorioGerado.setRegistros(this.registroService.getRegistrosNaData(mes));
		relatorioGerado.setAlocacoes(this.alocacaoService.getAlocacoesDoMes(mes));
		relatorioGerado.setHorasTrabalhadas(this.getSomaDosHorariosTrabalhados(mes).toString());
		relatorioGerado.setHorasDevidas(this.getHorasDevidas(mes).toString());
		relatorioGerado.setHorasExcedentes(this.getHorasExcedentes(mes).toString());
		
		return relatorioGerado;
	}

	public void validaFormatoDaDataInformada(String data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
		ValidacaoDeDatas validacaoDeDatas = new ValidacaoDeDatas(formatter);
		validacaoDeDatas.validaData(data);
	}
	
	public Duration getSomaDosHorariosTrabalhados(String mes) {
		Duration horasTrabalhadasNoMes = Duration.ZERO;
		for(Registro registro : this.registroService.getRegistrosNaData(mes)) {
			horasTrabalhadasNoMes = horasTrabalhadasNoMes.plus(this.momentoService.getHorasTrabalhadasNoMes(registro.getDia()));
		}
		return horasTrabalhadasNoMes;
	}
	
	public Duration getHorarioIdealTrabalhado(String mes) {
		Duration horario = Duration.ofHours(8);
		return horario.multipliedBy(this.registroService.getRegistrosNaData(mes).size());
	}
	
	public Duration getHorasExcedentes(String mes) {
		Duration horasIdeais = this.getHorarioIdealTrabalhado(mes);
		Duration horasTrabalhadas = this.getSomaDosHorariosTrabalhados(mes);
		if(this.getHorarioIdealTrabalhado(mes).compareTo(this.getSomaDosHorariosTrabalhados(mes)) == -1) {
			return horasTrabalhadas.minus(horasIdeais);
		}
		return Duration.ZERO;
	}
	
	// Recebe como parâmetro uma data no formado yyyy-MM e,
	// caso as horas ideais de trabalho (8 horas por dia) for maior do que as horas trabalhadas,
	// retorna a diferença entre o horario ideal e o horário trabalhado.
	public Duration getHorasDevidas(String mes) {
		Duration horasIdeais = this.getHorarioIdealTrabalhado(mes);
		Duration horasTrabalhadas = this.getSomaDosHorariosTrabalhados(mes);
		if(this.getHorarioIdealTrabalhado(mes).compareTo(this.getSomaDosHorariosTrabalhados(mes)) == 1) {
			return horasIdeais.minus(horasTrabalhadas);
		}
		return Duration.ZERO;
	}

}
