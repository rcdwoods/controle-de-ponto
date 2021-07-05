package br.com.controledeponto.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.controledeponto.model.Alocacao;
import br.com.controledeponto.model.Momento;
import br.com.controledeponto.service.AlocacaoService;

@RestController
@RequestMapping("/alocacoes")
public class AlocacoesResource {
	
	private AlocacaoService alocacaoService;
	
	public AlocacoesResource(AlocacaoService alocacaoService) {
		this.alocacaoService = alocacaoService;
	}
	
	@PostMapping
	public ResponseEntity<?> adicionarAlocacao(@RequestBody Alocacao alocacao) {
		Alocacao alocacaoAdicionada = this.alocacaoService.novaAlocacao(alocacao);
		return ResponseEntity.ok(alocacao);
	}
	
	@GetMapping
	public String buscarPonto() {
		return "Bom diaaa!";
	}

}
