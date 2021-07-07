package br.com.controledeponto.service;

import static org.junit.jupiter.api.Assertions.assertEquals; 
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.controledeponto.model.Momento;
import br.com.controledeponto.repository.MomentoRepository;
import br.com.controledeponto.service.exception.DataExistenteException;
import br.com.controledeponto.service.exception.DiaDaSemanaInvalidoException;
import br.com.controledeponto.service.exception.EstouroNoLimiteDeHorariosException;
import br.com.controledeponto.service.exception.HorarioDeAlmocoNaoAtingidoException;

class MomentoServiceTest {

	private MomentoRepository momentoRepository;
	private MomentoService momentoService;

	@BeforeEach
	public void setup() {
		this.momentoRepository = mock(MomentoRepository.class);
		this.momentoService = new MomentoService(this.momentoRepository);
	}

	@Test
	void deveLancarUmaDateTimeParseException_QuandoADataEstiverIncorreta() {
		Momento momento = new Momento("2018-13-22T00:00:00");
		assertThrows(DateTimeParseException.class, () -> this.momentoService.validaFormatoDaDataHora(momento));
	}

	@Test
	void deveLancarUmaDataExistenteException_QuandoOMomentoForExistente() {
		Momento momento = new Momento("2018-06-22T10:00:00");

		when(this.momentoRepository.existsByDataHora(momento.getDataHora())).thenReturn(true);

		assertThrows(DataExistenteException.class, () -> this.momentoService.validaExistenciaDaDataHora(momento.getDataHora()));
	}

	@Test
	void deveLancarUmaDiaDaSemanaInvalidoException_QuandoForEmUmSabado() {
		Momento momento = new Momento("2021-07-03T10:00:00");

		assertThrows(DiaDaSemanaInvalidoException.class, () -> this.momentoService.validaDiaDaSemana(momento));
	}

	@Test
	void deveLancarUmaDiaDaSemanaInvalidoException_QuandoForEmUmDomingo() {
		Momento momento = new Momento("2021-07-04T10:00:00");

		assertThrows(DiaDaSemanaInvalidoException.class, () -> this.momentoService.validaDiaDaSemana(momento));
	}

	@Test
	void deveLancarUmaEstouroNoLimiteDeHorariosException_QuandoJaExistiremQuatroMomentosNoMesmoDia() {
		Momento momento = new Momento("2021-07-07T14:00:00");
		List<Momento> momentosExistentes = List.of(new Momento("2021-07-07T10:00:00"),
				new Momento("2021-07-07T11:00:00"), new Momento("2021-07-07T12:00:00"),
				new Momento("2021-07-07T13:00:00"));

		when(this.momentoRepository.findAll()).thenReturn(momentosExistentes);

		assertThrows(EstouroNoLimiteDeHorariosException.class,
				() -> this.momentoService.validaHorariosRegistradosNoDia(momento));
	}

	@Test
	void deveLancarHorarioDeAlmocoNaoAtingidoException_QuandoHorarioForMenorQueUmaHora() {
		Momento momento = new Momento("2021-07-07T11:30:00");
		List<Momento> momentosExistentes = List.of(new Momento("2021-07-07T10:00:00"),
				new Momento("2021-07-07T11:00:00"), new Momento("2021-07-08T12:00:00"),
				new Momento("2021-07-08T13:00:00"));

		when(this.momentoRepository.findAll()).thenReturn(momentosExistentes);

		assertThrows(HorarioDeAlmocoNaoAtingidoException.class,
				() -> this.momentoService.validaHorarioDeAlmoco(momento));
	}

	@Test
	void naoDeveLancarHorarioDeAlmocoNaoAtingidoException_QuandoHorarioForMaiorOuIgualUmaHora() {
		Momento momento = new Momento("2021-07-07T12:00:00");
		List<Momento> momentosExistentes = List.of(new Momento("2021-07-07T10:00:00"),
				new Momento("2021-07-07T11:00:00"), new Momento("2021-07-08T12:00:00"),
				new Momento("2021-07-08T13:00:00"));

		when(this.momentoRepository.findAll()).thenReturn(momentosExistentes);

		this.momentoService.validaHorarioDeAlmoco(momento);
	}

	@Test
	void deveLancarUmaOrdemDeInsercaoInvalidaException_QuandoHorarioForMenorDoQueHorariosRegistrados() {

	}

	@Test
	void deveRetornarAQuantidadeDeHorasTrabalhadasNoDia_QuandoHouverQuatroHorarios() {
		List<Momento> momentosExistentes = List.of(new Momento("2021-07-07T08:00:00"),
				new Momento("2021-07-07T12:00:00"), new Momento("2021-07-07T13:00:00"),
				new Momento("2021-07-07T17:00:00"));

		when(this.momentoRepository.findAll()).thenReturn(momentosExistentes);

		LocalTime horasTrabalhadasObtidas = this.momentoService.getHorasTrabalhadasNaData("2021-07-07");
		assertEquals(LocalTime.of(8, 0, 0), horasTrabalhadasObtidas);
	}
	
	@Test
	void deveRetornarAQuantidadeDeHorasTrabalhadasNoDia_QuandoHouverDoisOuTresHorarios() {
		List<Momento> momentosExistentes = List.of(new Momento("2021-07-07T08:00:00"),
				new Momento("2021-07-07T12:00:00"), new Momento("2021-07-07T13:00:00"));

		when(this.momentoRepository.findAll()).thenReturn(momentosExistentes);
		
		LocalTime horasTrabalhadasObtidas = this.momentoService.getHorasTrabalhadasNaData("2021-07-07");
		assertEquals(LocalTime.of(4, 0, 0), horasTrabalhadasObtidas);
	}

}
