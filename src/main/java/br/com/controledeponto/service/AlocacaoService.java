package br.com.controledeponto.service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.controledeponto.dto.AlocacaoDTO;
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

	// Executa as validações.
	// Caso alguma falhe, sua devida exceção será lançada.
	public Alocacao novaAlocacao(Alocacao alocacao) {
		this.validaFormatoDaDataEHorarioDeAlocacao(alocacao);
		this.validaHorarioDeAlocacao(alocacao);
		return this.alocacaoRepository.save(alocacao);
	}

	// Valida se horário alocado está acima do trabalho no dia.
	// Caso esteja, a exceção HorarioAcimaDoTrabalhadoException será lançada.
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

	// Recebe uma Alocação, e valida o formato da data e do horário registrados nela.
	// Caso não estejam no formato indicado, será lançada uma DateTimeParseException.
	public void validaFormatoDaDataEHorarioDeAlocacao(Alocacao alocacao) {
		DateTimeFormatter padraoDaData = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.formatoDoHorario.parse(alocacao.getTempo());
		padraoDaData.parse(alocacao.getDia());
	}

	// Recebe uma data do tipo "yyyy-MM-dd" e retorna as horas alocadas nessa data.
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

	// Recebe uma data no formato "yyyy-MM" como parâmetro,
	// seleciona todas alocacoes do mês e une todas as alocações repetidas em uma única, que contém 
	// o total das horas trabalhadas em uma cada delas.
	public List<AlocacaoDTO> getAlocacoesDoMes(String mes) {
		List<Alocacao> alocacoesRegistradas = this.alocacaoRepository.findAll();

		List<String> nomeDasAlocacoesDoMes = alocacoesRegistradas.stream()
				.filter(alocacao -> alocacao.getDia().substring(0, 7).equals(mes)).map(Alocacao::getNomeProjeto).collect(Collectors.toList());
		
		List<String> nomesAlocacoesDoMesSemRepeticao = new ArrayList<>();
		
		for(String nomeDaAlocacao : nomeDasAlocacoesDoMes) {
			if(!nomesAlocacoesDoMesSemRepeticao.contains(nomeDaAlocacao)) {
				nomesAlocacoesDoMesSemRepeticao.add(nomeDaAlocacao);
			}
		}
		
		List<AlocacaoDTO> alocacoesDoMesSemRepeticao = nomesAlocacoesDoMesSemRepeticao.stream()
				.map(nomeDoProjeto -> new Alocacao("", this.getHorasTrabalhadasEmUmProjeto(nomeDoProjeto, mes).toString(), nomeDoProjeto))
				.map(alocacao -> new AlocacaoDTO(alocacao))
				.collect(Collectors.toList());

		return alocacoesDoMesSemRepeticao;
	}

	// Recebe uma data no formato "yyyy-DD" como parâmetro,
	// e retorna a soma de todas as horas alocadas em um mesmo projeto.
	public Duration getHorasTrabalhadasEmUmProjeto(String nomeDoProjeto, String mes) {
		List<Alocacao> alocacoesRegistradas = this.alocacaoRepository.findAll();
		List<Alocacao> alocacoesDoMesNoProjeto = alocacoesRegistradas.stream()
				.filter(alocacao -> alocacao.getDia().substring(0, 7).equals(mes))
				.filter(alocacao -> alocacao.getNomeProjeto().equals(nomeDoProjeto)).collect(Collectors.toList());

		List<Duration> horasTrabalhadasNoProjeto = alocacoesDoMesNoProjeto.stream().map(Alocacao::getTempo)
				.map(horas -> Duration.parse(horas)).collect(Collectors.toList());

		Duration somaDasHorasTrabalhadasNoProjeto = Duration.ZERO;

		for (Duration hora : horasTrabalhadasNoProjeto) {
			somaDasHorasTrabalhadasNoProjeto = somaDasHorasTrabalhadasNoProjeto.plus(hora);
		}

		return somaDasHorasTrabalhadasNoProjeto;
	}

}
