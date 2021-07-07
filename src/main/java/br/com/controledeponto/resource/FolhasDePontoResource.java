package br.com.controledeponto.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.controledeponto.model.Relatorio;
import br.com.controledeponto.service.RelatorioService;

@Controller
@RequestMapping("/folhas-de-ponto")
public class FolhasDePontoResource {
	
	@Autowired
	private RelatorioService relatorioService;
	
	@GetMapping("/{data}")
	public ResponseEntity<?> getRelatorioDoMes(@PathVariable String data) {
		Relatorio relatorioObtido = this.relatorioService.getRelatorioDoMes(data);
		return ResponseEntity.ok(relatorioObtido);
	}

}
