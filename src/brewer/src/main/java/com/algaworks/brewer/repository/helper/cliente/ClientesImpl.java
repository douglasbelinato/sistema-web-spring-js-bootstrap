package com.algaworks.brewer.repository.helper.cliente;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Cliente;
import com.algaworks.brewer.repository.filter.ClienteFilter;
import com.algaworks.brewer.repository.paginacao.PaginacaoUtil;

public class ClientesImpl implements ClientesQueries {
	
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@PersistenceContext
	private EntityManager manager;

	// Somente em métodos onde usamos a Criteria do Hibernate é que precisamos colocar o @Transactional, nos outros 
	// que usam as operações prontas do JPA não precisamos.
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	@Override
	public Page<Cliente> filtrar(ClienteFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cliente.class);
		
		paginacaoUtil.preparar(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		// É preciso incluir estes Left Joins para evitar erro de LAZY INITIALIZE EXCEPTION
		// ao tentar acessar os dados de cidade/estado na tela de consultar clientes.
		criteria.createAlias("endereco.cidade", "c", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("c.estado", "e", JoinType.LEFT_OUTER_JOIN);
		
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	private Long total(ClienteFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cliente.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount()); // conta a qtd de resultados
		return (Long) criteria.uniqueResult();
	}
	
	private void adicionarFiltro(ClienteFilter filtro, Criteria criteria) {
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
			
			if (!StringUtils.isEmpty(filtro.getCpfCnpj())) {
				criteria.add(Restrictions.eq("cpfCnpj", filtro.getCpfCnpj()));
			}
		}
	}

}
