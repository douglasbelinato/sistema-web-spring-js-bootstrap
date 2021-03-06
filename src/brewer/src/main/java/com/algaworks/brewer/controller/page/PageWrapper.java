package com.algaworks.brewer.controller.page;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.util.UriComponentsBuilder;

public class PageWrapper<T> {
	
	private Page<T> page;
	private UriComponentsBuilder uriBuilder;

	public PageWrapper(Page<T> page, HttpServletRequest httpServletRequest) {
		this.page = page;
		// --------------------------------------------------------------------------------
		// this.uriBuilder = ServletUriComponentsBuilder.fromRequest(httpServletRequest);
		// Para corrigir bug nascido da paginação:
		
		// obtém parâmetros na requisicao get (url), se houver
		String queryString = httpServletRequest.getQueryString() != null ? "?" + httpServletRequest.getQueryString() : "";
		
		// Para buscas onde se usa palavras com espaço, é substituído o sinal de + no espaçamento pelo %20
		// para evitar exceção:
		// java.lang.IllegalArgumentException: Invalid character '+' for QUERY_PARAM 
		String httpUrl = httpServletRequest.getRequestURL().append(queryString).toString().replaceAll("\\+", "%20");
		
		this.uriBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl);
		// --------------------------------------------------------------------------------
				
	}
	
	public List<T> getConteudo() {
		return page.getContent();
	}
	
	public boolean isVazia() {
		return page.getContent().isEmpty();
	}
	
	public int getAtual() {
		return page.getNumber();
	}
	
	public boolean isPrimeira() {
		return page.isFirst();
	}
	
	public boolean isUltima() {
		return page.isLast();
	}
	
	public int getTotal() {
		return page.getTotalPages();
	}
	
	public String urlParaPagina(int pagina) {
		// .build(true).encode() para formatar valores passados via GET no encode correto (exemplo: valores numericos)
		// url --> ?page={pagina}
		return uriBuilder.replaceQueryParam("page", pagina).build(true).encode().toUriString();
	}
	
	public String urlOrdenada(String propriedade) {
		UriComponentsBuilder uriBuilderOrder = UriComponentsBuilder
											 	.fromUriString(uriBuilder.build(true).encode().toUriString());
		
		String valorSort = String.format("%s,%s", propriedade, inverterDirecao(propriedade));
		
		// url --> ?sort=propriedade,{asc|desc}
		return uriBuilderOrder.replaceQueryParam("sort", valorSort).build(true).encode().toUriString();
	}
	
	public String inverterDirecao(String propriedade) {
		String direcao = "asc";
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
		
		if (order != null) {
			direcao = Sort.Direction.ASC.equals(order.getDirection()) ? "desc" : "asc"; 
		}
		
		return direcao;
	}
	
	public boolean descendente(String propriedade) {
		return inverterDirecao(propriedade).equals("asc");
	}
	
	public boolean ordenada(String propriedade) {
		Order order = page.getSort() != null ? page.getSort().getOrderFor(propriedade) : null;
		
		if (order == null) {
			return false;
		}
		
		return page.getSort().getOrderFor(propriedade) != null ? true : false;
	}

}
