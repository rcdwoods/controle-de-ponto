package br.com.controledeponto.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.controledeponto.model.Momento;
import br.com.controledeponto.repository.MomentoRepository;
import br.com.controledeponto.service.exception.DataExistenteException;
import br.com.controledeponto.service.exception.DiaDaSemanaInvalidoException;
import br.com.controledeponto.service.exception.EstouroNoLimiteDeHorariosException;
import br.com.controledeponto.service.exception.HorarioDeAlmocoNaoAtingidoException;
import br.com.controledeponto.service.exception.OrdemDeInsercaoInvalidaException;
import br.com.controledeponto.validator.ValidacaoDeDatas;

@Service
public class MomentoService {

	private MomentoRepository momentoRepository;

	public MomentoService(MomentoRepository momentoRepository) {
		this.momentoRepository = momentoRepository;
	}

	public Momento adicionarMomento(Momento momento) {
		this.validaMomento(momento);
		return this.momentoRepository.save(momento);
	}

	// Faz a validação geral da inserção do momento.
	public void validaMomento(Momento momento) {
		this.validaFormatoDaDataHora(momento);
		this.validaExistenciaDaDataHora(momento.getDataHora());
		this.validaHorariosRegistradosNoDia(momento);
		this.validaDiaDaSemana(momento);
		this.validaOrdemDeInsercaoDosHorarios(momento);
		if (this.obterMomentosRegistradosNaData(momento.getDataHora()).size() == 2) {
			this.validaHorarioDeAlmoco(momento);
		}
	}

	public void validaFormatoDaDataHora(Momento momento) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		ValidacaoDeDatas validacaoDeDatas = new ValidacaoDeDatas(formatter);
		validacaoDeDatas.validaData(momento.getDataHora());
	}

	// Verifica se a data e hora do momento informado já existe.
	// Caso exista, lança uma DataExistenteException.
	public void validaExistenciaDaDataHora(String data) {
		if (this.momentoRepository.existsByDataHora(data)) {
			throw new DataExistenteException();
		}
	}

	// Verifica horários já registrados no dia.
	// Caso haja mais de 4 horários registrados, lança uma
	// EstouroNoLimiteDeHorariosException.
	public void validaHorariosRegistradosNoDia(Momento momento) {
		List<Momento> momentosDoMesmoDia = this.obterMomentosRegistradosNaData(momento.getDataHora());

		if (momentosDoMesmoDia.size() == 4) {
			throw new EstouroNoLimiteDeHorariosException();
		}
	}

	// Verifica se momento está sendo inserido em finais de semana
	// Caso esteja, lança uma DiaDaSemanaInvalidoException.
	public void validaDiaDaSemana(Momento momento) {
		LocalDateTime dataDoMomento = LocalDateTime.parse(momento.getDataHora());
		if (dataDoMomento.getDayOfWeek() == DayOfWeek.SATURDAY || dataDoMomento.getDayOfWeek() == DayOfWeek.SUNDAY) {
			throw new DiaDaSemanaInvalidoException();
		}
	}

	// Verifica se houve ao menos uma hora de almoço.
	// Caso não tenha ocorrido, lança uma HorarioDeAlmocoNaoAtingidoException.
	public void validaHorarioDeAlmoco(Momento momento) {
		List<Momento> momentosRegistrados = this.obterMomentosRegistradosNaData(momento.getDataHora());
		LocalDateTime segundoPonto = LocalDateTime.parse(momentosRegistrados.get(1).getDataHora());
		LocalDateTime terceiroPonto = LocalDateTime.parse(momento.getDataHora());

		Long segundosEntreMomentos = Duration.between(segundoPonto, terceiroPonto).getSeconds();

		if ((segundosEntreMomentos / 3600) < 1) {
			throw new HorarioDeAlmocoNaoAtingidoException();
		}
	}

	// Verifica se a ordem de inserção dos horários está correta.
	// Por exemplo, um funcionário não pode bater um ponto uma hora atrás do último
	// ponto.
	// Logo, deve seguir uma ordem númerica válida, como 12:00, 16:00, 17:00, 20:00.
	public void validaOrdemDeInsercaoDosHorarios(Momento momento) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		List<Momento> momentosRegistradosNoDia = this.obterMomentosRegistradosNaData(momento.getDataHora());
		List<String> momentosComHorarioSuperiorAoInformado = momentosRegistradosNoDia.stream().map(Momento::getDataHora)
				.filter(item -> LocalDateTime.parse(item, formatter)
						.isAfter(LocalDateTime.parse(momento.getDataHora(), formatter)))
				.collect(Collectors.toList());
		if (momentosComHorarioSuperiorAoInformado.size() > 0) {
			throw new OrdemDeInsercaoInvalidaException();
		}
	}

	// Recebe uma data no formato "yyyy-MM-dd" ou "yyyy-MM-dd..."
	// e retorna uma lista de momentos registrados na data informada
	public List<Momento> obterMomentosRegistradosNaData(String data) {
		List<Momento> momentos = this.momentoRepository.findAll();
		String dataDoMomento = data.substring(0, 10);

		List<Momento> momentosDoMesmoDia = momentos.stream()
				.filter(item -> item.getDataHora().substring(0, 10).equals(dataDoMomento)).collect(Collectors.toList());

		return momentosDoMesmoDia;
	}

	public List<Momento> getMomentosRegistradosNoMes(String data) {
		List<Momento> momentos = this.momentoRepository.findAll();
		String dataDoMomento = data.substring(0, 7);

		List<Momento> momentosDoMesmoMes = momentos.stream()
				.filter(item -> item.getDataHora().substring(0, 7).equals(dataDoMomento)).collect(Collectors.toList());

		return momentosDoMesmoMes;
	}

	// Recebe uma dataHora no formato "2018-08-22T08:00:00" como parâmetro
	// e retorna a quantidade total de horas trabalhadas por um funcionário em
	// determinada data.
	// A quantidade de horas trabalhadas é calculada por meio da soma das diferenças
	// entre os dois pontos de entrada e saída do funcionário.
	public Duration getHorasTrabalhadasNaData(String data) {
		List<Momento> momentosRegistradosNoDia = this.obterMomentosRegistradosNaData(data);

		if (momentosRegistradosNoDia.size() > 2) {
			LocalTime primeiroPonto = LocalTime.parse(momentosRegistradosNoDia.get(0).getDataHora().substring(11, 19));
			LocalTime segundoPonto = LocalTime.parse(momentosRegistradosNoDia.get(1).getDataHora().substring(11, 19));
			Duration primeiraDiferenca = Duration.between(primeiroPonto, segundoPonto);

			if (momentosRegistradosNoDia.size() == 4) {
				LocalTime terceiroPonto = LocalTime
						.parse(momentosRegistradosNoDia.get(2).getDataHora().substring(11, 19));
				LocalTime quartoPonto = LocalTime
						.parse(momentosRegistradosNoDia.get(3).getDataHora().substring(11, 19));

				return primeiraDiferenca.plus(Duration.between(terceiroPonto, quartoPonto));
			}
			return primeiraDiferenca;
		}
		return Duration.ZERO;
	}

	// Buscar horas trabalhadas no mês
	public Duration getHorasTrabalhadasNoMes(String mes) {
		List<Momento> momentosRegistradosNoDia = this.obterMomentosRegistradosNaData(mes);

		if (momentosRegistradosNoDia.size() >= 2) {
			LocalTime primeiroPonto = LocalTime.parse(momentosRegistradosNoDia.get(0).getDataHora().substring(11, 19));
			LocalTime segundoPonto = LocalTime.parse(momentosRegistradosNoDia.get(1).getDataHora().substring(11, 19));
			Duration primeiraDiferenca = Duration.between(primeiroPonto, segundoPonto);

			if (momentosRegistradosNoDia.size() == 4) {
				LocalTime terceiroPonto = LocalTime
						.parse(momentosRegistradosNoDia.get(2).getDataHora().substring(11, 19));
				LocalTime quartoPonto = LocalTime
						.parse(momentosRegistradosNoDia.get(3).getDataHora().substring(11, 19));
				Duration segundaDiferenca = Duration.between(terceiroPonto, quartoPonto);

				return primeiraDiferenca.plus(segundaDiferenca);
			}
			return primeiraDiferenca;
		}
		return Duration.ZERO;
	}

}
