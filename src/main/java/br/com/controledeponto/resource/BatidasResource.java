package br.com.controledeponto.resource;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.controledeponto.model.Momento;
import br.com.controledeponto.service.MomentoService;

@RestController
@RequestMapping("/batidas")
public class BatidasResource {
	
	private MomentoService momentoService;
	
	public BatidasResource(MomentoService momentoService) {
		this.momentoService = momentoService;
	}
	
	@PostMapping
	public ResponseEntity<?> adicionarBatidaDePonto(@Valid @RequestBody Momento momento) {
//		LocalDateTime dataInformada = LocalDateTime.parse(momento.getDataHora());
		Momento momentoAdicionado = this.momentoService.adicionarMomento(momento);
		return ResponseEntity.ok(momentoAdicionado);
	}

}
