package com.algaworks.brewer.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.algaworks.brewer.model.Estado;

// Converter deve ser adicionado no webconfig da aplicação
public class EstadoConverter implements Converter<String, Estado> {
	
	@Override
	public Estado convert(String codigo) {
		// nessa versão do Spring o converter só é chamado se o codigo for != null
		// Mas mesmo assim adicionamos esse if por precaução
		if (!StringUtils.isEmpty(codigo)) {
			Estado estado = new Estado();
			estado.setCodigo(Long.valueOf(codigo));
			return estado;
		}
		
		return null;
	}

}
