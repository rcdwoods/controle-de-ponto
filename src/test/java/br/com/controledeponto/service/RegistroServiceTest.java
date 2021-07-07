package br.com.controledeponto.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.Registro;
import br.com.controledeponto.repository.MomentoRepository;
import br.com.controledeponto.service.exception.RelatorioInexistenteException;

class RegistroServiceTest {

	private MomentoRepository momentoRepository;
	private MomentoService momentoService;
	private RegistroService registroService;

	@BeforeEach
	void setup() {
		this.momentoRepository = mock(MomentoRepository.class);
		this.momentoService = new MomentoService(this.momentoRepository);
		this.registroService = new RegistroService(this.momentoRepository, this.momentoService);
	}

	@Test
	void deveRetornarUmNovoRegistro_QuandoExistir() {
		List<Momento> momentosExistentes = List.of(new Momento("2021-07-07T10:00:00"),
				new Momento("2021-07-07T11:00:00"), new Momento("2021-07-08T12:00:00"),
				new Momento("2021-07-08T13:00:00"));

		List<Registro> registrosEsperados = List.of(new Registro("2021-07-07", Arrays.asList("10:00:00", "11:00:00")),
				new Registro("2021-07-08", Arrays.asList("12:00:00", "13:00:00")));

		when(this.momentoRepository.findAll()).thenReturn(momentosExistentes);

		assertEquals(registrosEsperados, this.registroService.getRegistrosNaData("2021-07-07"));
	}

}
