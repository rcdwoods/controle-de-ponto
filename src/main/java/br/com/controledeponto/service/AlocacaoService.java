package br.com.controledeponto.service;

import org.springframework.stereotype.Service;

import br.com.controledeponto.model.Alocacao;
import br.com.controledeponto.model.Momento;
import br.com.controledeponto.repository.AlocacaoRepository;

@Service
public class AlocacaoService {
	
	private AlocacaoRepository alocacaoRepository;
	
	public AlocacaoService(AlocacaoRepository alocacaoRepository) {
		this.alocacaoRepository = alocacaoRepository;
	}
	
	public Alocacao novaAlocacao(Alocacao alocacao) {
		return this.alocacaoRepository.save(alocacao);
	}

}
