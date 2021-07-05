package br.com.controledeponto.service;

import org.springframework.stereotype.Service;

import br.com.controledeponto.model.Momento;
import br.com.controledeponto.repository.MomentoRepository;

@Service
public class MomentoService {
	
	private MomentoRepository momentoRepository;
	
	public MomentoService(MomentoRepository momentoRepository) {
		this.momentoRepository = momentoRepository;
	}
	
	public Momento adicionarMomento(Momento momento) {
		return this.momentoRepository.save(momento);
	}

}
