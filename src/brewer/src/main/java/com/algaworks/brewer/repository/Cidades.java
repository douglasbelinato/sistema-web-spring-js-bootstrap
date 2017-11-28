package com.algaworks.brewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.brewer.model.Cidade;

public interface Cidades extends JpaRepository<Cidade, Long> {

	// Usando a construção automática de query do Spring Data
	// só preciso colocar findBy Estado (que é um objeto dentro
	// da classe Cidade), e depois o nome do atributo do objeto
	// Estado que deve ser usado para fazer a busca, que no caso
	// é o atributo 'codigo'. Por isso findByEstadoCodigo.
	public List<Cidade> findByEstadoCodigo(Long codigoEstado);
}
