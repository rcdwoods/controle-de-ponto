package br.com.controledeponto.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.controledeponto.model.Alocacao;
import br.com.controledeponto.repository.AlocacaoRepository;
import br.com.controledeponto.service.exception.HorarioAcimaDoTrabalhadoException;

@Service
public class AlocacaoService {

	private AlocacaoRepository alocacaoRepository;
	private MomentoService momentoService;

	private DateTimeFormatter formatoDoHorario = DateTimeFormatter.ofPattern("'PT'HH'H'mm'M'ss'S'");

	public AlocacaoService(AlocacaoRepository alocacaoRepository, MomentoService momentoService) {
		this.alocacaoRepository = alocacaoRepository;
		this.momentoService = momentoService;
	}

	public Alocacao novaAlocacao(Alocacao alocacao) {
		this.validaFormatoDaDataEHorarioDeAlocacao(alocacao);
		this.validaHorarioDeAlocacao(alocacao);
		return this.alocacaoRepository.save(alocacao);
	}

	public void validaHorarioDeAlocacao(Alocacao alocacao) {
		LocalTime horasTrabalhadas = this.momentoService.getHorasTrabalhadasNaData(alocacao.getDia());
		LocalTime horasAlocadasNaData = this.getHorasAlocadasNaData(alocacao.getDia());
		LocalTime horasParaAlocacao = LocalTime.parse(alocacao.getTempo(), this.formatoDoHorario);
		LocalTime horasAlocadasComHoraAdicionada = horasAlocadasNaData.plusHours(horasParaAlocacao.getHour())
				.plusMinutes(horasParaAlocacao.getMinute()).plusSeconds(horasParaAlocacao.getSecond());
		
		if ((horasParaAlocacao.compareTo(horasTrabalhadas) == 1)
				|| (horasAlocadasComHoraAdicionada.compareTo(horasTrabalhadas) == 1)) {
			throw new HorarioAcimaDoTrabalhadoException();
		}
	}

	public void validaFormatoDaDataEHorarioDeAlocacao(Alocacao alocacao) {
		DateTimeFormatter padraoDaData = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.formatoDoHorario.parse(alocacao.getTempo());
		padraoDaData.parse(alocacao.getDia());
	}

	// Recebe uma data e retorna as horas alocadas para essa semana data.
	public LocalTime getHorasAlocadasNaData(String data) {
		List<Alocacao> alocacoesRegistradas = this.alocacaoRepository.findAll();
		LocalTime horasAlocadasNaData = LocalTime.parse("PT00H00M00S", this.formatoDoHorario);

		List<Alocacao> alocacoesNaData = alocacoesRegistradas.stream()
				.filter(alocacao -> alocacao.getDia().equals(data)).collect(Collectors.toList());

		for (Alocacao alocacao : alocacoesNaData) {
			LocalTime horarioAlocado = LocalTime.parse(alocacao.getTempo(), this.formatoDoHorario);
			horasAlocadasNaData = horasAlocadasNaData.plusHours(horarioAlocado.getHour())
					.plusMinutes(horarioAlocado.getMinute()).plusSeconds(horarioAlocado.getSecond());
		}

		return horasAlocadasNaData;

	}

}
