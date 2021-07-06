package br.com.controledeponto.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.controledeponto.model.Alocacao;

public interface AlocacaoRepository extends MongoRepository<Alocacao, String>{
	
	public boolean existsByDia(String dia);

}
