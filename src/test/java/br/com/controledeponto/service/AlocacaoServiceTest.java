package br.com.controledeponto.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.controledeponto.repository.AlocacaoRepository;

class AlocacaoServiceTest {

	private AlocacaoRepository alocacaoRepository;
	private AlocacaoService alocacaoService;

	@BeforeEach
	public void setup() {
		this.alocacaoRepository = mock(AlocacaoRepository.class);
		this.alocacaoService = new AlocacaoService(this.alocacaoRepository);

//		when(this.usuarioRepository.findByUsernameIgnoreCase("Gabriel")).thenReturn(this.primeiroUsuario);
	}

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
