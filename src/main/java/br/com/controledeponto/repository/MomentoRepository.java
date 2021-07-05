package br.com.controledeponto.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.controledeponto.model.Momento;

public interface MomentoRepository extends MongoRepository<Momento, String>{

}
