package br.com.controledeponto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.controledeponto.model.Alocacao;
import br.com.controledeponto.repository.AlocacaoRepository;
import br.com.controledeponto.repository.MomentoRepository;

class AlocacaoServiceTest {

	private AlocacaoRepository alocacaoRepository;
	private AlocacaoService alocacaoService;

	private MomentoRepository momentoRepository;
	private MomentoService momentoService;

	@BeforeEach
	public void setup() {
		this.alocacaoRepository = mock(AlocacaoRepository.class);
		this.alocacaoService = new AlocacaoService(this.alocacaoRepository, this.momentoService);

		this.momentoRepository = mock(MomentoRepository.class);
		this.momentoService = new MomentoService(this.momentoRepository);
	}

	@Test
	void deveLancarHorarioAcimaDoTrabalhadoException_QuandoHorarioInformadoForMaiorQueOAlocado() {
		List<Alocacao> alocacoesRegistradas = List.of(new Alocacao("2020-07-25", "PT04H00M00S", "Projeto"),
				new Alocacao("2020-07-25", "PT02H00M00S", "Projeto"),
				new Alocacao("2020-07-25", "PT02H00M00S", "Projeto"));

	}

	@Test
	void deveRetornarAQuantidadeCorretaDeHorasAlocadasNaData_QuandoOHorarioForValido() {
		List<Alocacao> alocacoesRegistradas = List.of(new Alocacao("2020-07-25", "PT04H00M00S", "Projeto"),
				new Alocacao("2020-07-25", "PT02H00M00S", "Projeto"),
				new Alocacao("2020-07-25", "PT02H00M00S", "Projeto"));

		when(this.alocacaoRepository.findAll()).thenReturn(alocacoesRegistradas);

		Duration quantidadeDeHorasAlocadasNaData = this.alocacaoService.getHorasAlocadasNaData("2020-07-25");

		assertEquals(Duration.ofHours(8),
				quantidadeDeHorasAlocadasNaData);
	}

	@Test
	void deveRetornarHorasTrabalhadasEmUmProjeto_QuandoHouveremAlocacoesNoProjeto() {
		List<Alocacao> alocacoesRegistradas = List.of(new Alocacao("2020-07-25", "PT02H00M00S", "Projeto"),
				new Alocacao("2020-07-25", "PT02H00M00S", "Projeto"),
				new Alocacao("2020-07-25", "PT02H00M00S", "Projeto"));
		
		when(this.alocacaoRepository.findAll()).thenReturn(alocacoesRegistradas);
		
		Duration horasTrabalhadasNoProjeto = this.alocacaoService.getHorasTrabalhadasEmUmProjetoNoMes("Projeto", "2020-07");
		
		assertEquals(Duration.ofHours(6), horasTrabalhadasNoProjeto);
	}

}
