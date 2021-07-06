package br.com.controledeponto.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.controledeponto.model.Momento;

public interface MomentoRepository extends MongoRepository<Momento, String>{
	
	public boolean existsByDataHora(String dataHora);

}
