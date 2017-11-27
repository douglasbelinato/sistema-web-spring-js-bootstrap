package com.algaworks.brewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algaworks.brewer.model.Estado;

/**
 * @Repository não é obrigatório. Funciona sem usar essa annotation também,
 * pois aqui o Spring vai achar e criar os beans apenas implementando
 * a interface JpaRepository.  
 *
 */
// @Repository
public interface Estados extends JpaRepository<Estado, Long> {

}
