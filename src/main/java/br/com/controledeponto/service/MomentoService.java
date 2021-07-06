package br.com.controledeponto.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.controledeponto.model.Momento;
import br.com.controledeponto.repository.MomentoRepository;
import br.com.controledeponto.service.exception.DataExistenteException;
import br.com.controledeponto.service.exception.DiaDaSemanaInvalidoException;
import br.com.controledeponto.service.exception.EstouroNoLimiteDeHorariosException;
import br.com.controledeponto.service.exception.HorarioDeAlmocoNaoAtingidoException;
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
		this.validaExistenciaDaDataHora(momento);
		this.validaHorariosRegistradosNoDia(momento);
		this.validaDiaDaSemana(momento);
		if(this.obterMomentosRegistradosDoDia(momento).size() > 2) {
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
	public void validaExistenciaDaDataHora(Momento momento) {
		if (this.momentoRepository.existsByDataHora(momento.getDataHora())) {
			throw new DataExistenteException();
		}
	}

	// Verifica horários já registrados no dia.
	// Caso haja mais de 4 horários registrados, lança uma
	// EstouroNoLimiteDeHorariosException.
	public void validaHorariosRegistradosNoDia(Momento momento) {
		List<Momento> momentosDoMesmoDia = this.obterMomentosRegistradosDoDia(momento);

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
		List<Momento> momentosRegistrados = this.momentoRepository.findAll();
		int horaDoUltimoMomento = LocalDateTime.parse(momentosRegistrados.get(1).getDataHora()).getHour();
		if ((LocalDateTime.parse(momento.getDataHora()).getHour() - horaDoUltimoMomento) == 1) {
			throw new HorarioDeAlmocoNaoAtingidoException();
		}
	}

	// Verifica se a ordem de inserção dos horários está correta.
	// Por exemplo, um funcionário não pode bater um ponto uma hora atrás do último ponto.
	// Logo, deve seguir uma ordem númerica, como 12:00, 16:00, 17:00, 20:00.
	public void validaOrdemDeInsercaoDosHorarios(Momento momento) {

	}
	
	// Recebe um momento como parâmetro
	// e retorna uma lista de momentos registrados no mesmo dia.
	public List<Momento> obterMomentosRegistradosDoDia(Momento momento) {
		List<Momento> momentos = this.momentoRepository.findAll();
		String dataDoMomento = momento.getDataHora().substring(0, 10);
		
		List<Momento> momentosDoMesmoDia = momentos.stream()
				.filter(item -> item.getDataHora().substring(0, 10).equals(dataDoMomento)).collect(Collectors.toList());
		
		return momentosDoMesmoDia;
	}

}
