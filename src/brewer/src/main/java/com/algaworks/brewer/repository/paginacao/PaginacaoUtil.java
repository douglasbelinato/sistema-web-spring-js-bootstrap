package com.algaworks.brewer.repository.paginacao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

// Anoto como @Component para que o Spring possa injetá-la em outras
// classes / objetos 
@Component
public class PaginacaoUtil {
	
	public void preparar(Criteria criteria, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;
		
		// Limitando a quantidade de resultados por página com criteria
		criteria.setFirstResult(primeiroRegistro); 		 // A partir de qual resultado eu inicio a exibição
		criteria.setMaxResults(totalRegistrosPorPagina); // Máximo de resultados POR PÁGINA
		
		Sort sort = pageable.getSort();
		
		if (sort != null){
			Sort.Order order = sort.iterator().next(); // Pode haver vários campos para ordenar
			String property = order.getProperty(); // Obtém o campo para ordernar
			criteria.addOrder(order.isAscending() ? Order.asc(property) : Order.desc(property));
		}
	}

}
