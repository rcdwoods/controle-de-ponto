package br.com.controledeponto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.controledeponto.model.Momento;
import br.com.controledeponto.model.Registro;
import br.com.controledeponto.repository.MomentoRepository;
import br.com.controledeponto.service.exception.RelatorioInexistenteException;

@Service
public class RegistroService {

	private MomentoRepository momentoRepository;
	private MomentoService momentoService;

	public RegistroService(MomentoRepository momentoRepository, MomentoService momentoService) {
		this.momentoRepository = momentoRepository;
		this.momentoService = momentoService;
	}

	// Recebe uma data como parâmetro
	// e retorna um objeto Registro com base nos momentos registrados nesta mesma
	// data.
	public List<Registro> getRegistrosNaData(String data) {
		List<Momento> momentos = this.momentoService.getMomentosRegistradosNoMes(data);
		List<String> datasQueContemMomentos = new ArrayList<>();
		
		for(Momento momento : momentos) {
			if(!datasQueContemMomentos.contains(momento.getDataHora().substring(0, 10))) {
				datasQueContemMomentos.add(momento.getDataHora().substring(0, 10));
			}
		}
		
		List<Registro> registros = datasQueContemMomentos.stream()
				.map(dataQueContemMomento -> new Registro(dataQueContemMomento, this.getHorariosDoRegistroNaDataHora(dataQueContemMomento)))
				.collect(Collectors.toList());
		return registros;
	}

	public List<String> getHorariosDoRegistroNaDataHora(String dataHora) {
		List<Momento> momentoRegistradoNaDataHora = this.momentoService.obterMomentosRegistradosNaData(dataHora);
		List<String> horariosRegistradosNaDataHora = momentoRegistradoNaDataHora.stream().map(Momento::getDataHora)
				.map(data -> data.substring(11, 19)).collect(Collectors.toList());
		return horariosRegistradosNaDataHora;
	}

	public void validaExistenciaDeRegistrosNoMes(String data) {
		List<Momento> momentosRegistrados = this.momentoRepository.findAll();
		List<Momento> momentosNaData = momentosRegistrados.stream()
				.filter(momento -> momento.getDataHora().substring(0, 7).equals(data)).collect(Collectors.toList());
		if (momentosNaData.isEmpty()) {
			throw new RelatorioInexistenteException();
		}
	}

}
