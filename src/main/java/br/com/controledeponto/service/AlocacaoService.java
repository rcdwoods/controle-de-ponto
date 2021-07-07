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
		Duration horasTrabalhadas = this.momentoService.getHorasTrabalhadasNaData(alocacao.getDia());
		Duration horasAlocadasNaData = this.getHorasAlocadasNaData(alocacao.getDia());
		Duration horasParaAlocacao = Duration.parse(alocacao.getTempo());
		Duration horasAlocadasComHoraAdicionada = horasParaAlocacao.plus(horasAlocadasNaData);

		if ((horasParaAlocacao.compareTo(horasTrabalhadas) == 1)
				|| (horasAlocadasComHoraAdicionada.compareTo(horasTrabalhadas) == 1)) {
			throw new HorarioAcimaDoTrabalhadoException();
		}
	}

	// Recebe uma Alocação, e valida o formato da data e do horário registrados
	// nela.
	// Caso não estejam no formato indicado, será lançada uma
	// DateTimeParseException.
	public void validaFormatoDaDataEHorarioDeAlocacao(Alocacao alocacao) {
		DateTimeFormatter padraoDaData = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		this.formatoDoHorario.parse(alocacao.getTempo());
		Duration tempo = Duration.parse(alocacao.getTempo());
		padraoDaData.parse(alocacao.getDia());
	}

	// Recebe uma data do tipo "yyyy-MM-dd" e retorna as horas alocadas nessa data.
	public Duration getHorasAlocadasNaData(String data) {
		List<Alocacao> alocacoesRegistradas = this.alocacaoRepository.findAll();
		Duration horasAlocadasNaData = Duration.ZERO;

		List<Alocacao> alocacoesNaData = alocacoesRegistradas.stream()
				.filter(alocacao -> alocacao.getDia().equals(data)).collect(Collectors.toList());

		for (Alocacao alocacao : alocacoesNaData) {
			Duration horarioAlocado = Duration.parse(alocacao.getTempo());
			horasAlocadasNaData = horasAlocadasNaData.plus(horarioAlocado);
		}

		return horasAlocadasNaData;
	}

	// Recebe uma data no formato "yyyy-MM" como parâmetro,
	// seleciona todas alocacoes do mês e une todas as alocações repetidas em uma
	// única, que contém
	// o total das horas trabalhadas em uma cada delas.
	public List<AlocacaoDTO> getAlocacoesDoMes(String mes) {
		List<Alocacao> alocacoesRegistradas = this.alocacaoRepository.findAll();

		List<String> nomeDasAlocacoesDoMes = alocacoesRegistradas.stream()
				.filter(alocacao -> alocacao.getDia().substring(0, 7).equals(mes)).map(Alocacao::getNomeProjeto)
				.collect(Collectors.toList());

		List<String> nomesAlocacoesDoMesSemRepeticao = new ArrayList<>();

		for (String nomeDaAlocacao : nomeDasAlocacoesDoMes) {
			if (!nomesAlocacoesDoMesSemRepeticao.contains(nomeDaAlocacao)) {
				nomesAlocacoesDoMesSemRepeticao.add(nomeDaAlocacao);
			}
		}

		List<AlocacaoDTO> alocacoesDoMesSemRepeticao = nomesAlocacoesDoMesSemRepeticao.stream()
				.map(nomeDoProjeto -> new Alocacao("",
						this.getHorasTrabalhadasEmUmProjetoNoMes(nomeDoProjeto, mes).toString(), nomeDoProjeto))
				.map(alocacao -> new AlocacaoDTO(alocacao)).collect(Collectors.toList());

		return alocacoesDoMesSemRepeticao;
	}

	// Recebe uma data no formato "yyyy-DD" como parâmetro,
	// e retorna a soma de todas as horas alocadas em um mesmo projeto.
	public Duration getHorasTrabalhadasEmUmProjetoNoMes(String nomeDoProjeto, String mes) {
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
