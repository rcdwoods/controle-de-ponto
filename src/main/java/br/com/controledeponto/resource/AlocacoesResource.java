package br.com.controledeponto.resource;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.controledeponto.model.Alocacao;
import br.com.controledeponto.model.Mensagem;
import br.com.controledeponto.service.AlocacaoService;

@RestController
@RequestMapping("/alocacoes")
public class AlocacoesResource {

	private AlocacaoService alocacaoService;

	public AlocacoesResource(AlocacaoService alocacaoService) {
		this.alocacaoService = alocacaoService;
	}

	@PostMapping
	public ResponseEntity<?> adicionarAlocacao(@Valid @RequestBody Alocacao alocacao) {
		this.alocacaoService.novaAlocacao(alocacao);
		return ResponseEntity.status(HttpStatus.CREATED).body(new Mensagem("Horas alocadas ao projeto"));
	}

}
