package com.algaworks.brewer.repository.helper.estilo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.model.Estilo;
import com.algaworks.brewer.repository.filter.EstiloFilter;

public class EstilosImpl implements EstilosQueries {
	
	@PersistenceContext
	private EntityManager manager;

	// Somente em métodos onde usamos a Criteria do Hibernate é que precisamos colocar o @Transactional, nos outros 
	// que usam as operações prontas do JPA não precisamos.
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	@Override
	public Page<Estilo> filtrar(EstiloFilter filtro, org.springframework.data.domain.Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Estilo.class);
		
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
		
		// Limitando a quantidade de resultados por página com criteria
		criteria.setFirstResult(primeiroRegistro); 		 // A partir de qual resultado eu inicio a exibição
		criteria.setMaxResults(totalRegistrosPorPagina); // Máximo de resultados POR PÁGINA
		
		adicionarFiltro(filtro, criteria);
		
		Sort sort = pageable.getSort();
		
		if (sort != null){
			Sort.Order order = sort.iterator().next(); // Pode haver vários campos para ordenar
			String property = order.getProperty(); // Obtém o campo para ordernar
			criteria.addOrder(order.isAscending() ? Order.asc(property) : Order.desc(property));
		}
		
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	private Long total(EstiloFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cerveja.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount()); // conta a qtd de resultados
		return (Long) criteria.uniqueResult();
	}
	
	private void adicionarFiltro(EstiloFilter filtro, Criteria criteria) {
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
		}
	}

}
