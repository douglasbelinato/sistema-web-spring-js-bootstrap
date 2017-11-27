package com.algaworks.brewer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.helper.estilo.EstilosQueries;

/**
 * @Repository não é obrigatório. Funciona sem usar essa annotation também,
 * pois aqui o Spring vai achar e criar os beans apenas implementando
 * a interface JpaRepository.  
 *
 */
@Repository
public interface Estilos extends JpaRepository<Estilo, Long>, EstilosQueries {
	
	public Optional<Estilo> findByNomeIgnoreCase(String nome);

}