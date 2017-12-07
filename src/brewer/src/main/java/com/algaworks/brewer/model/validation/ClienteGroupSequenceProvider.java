package com.algaworks.brewer.model.validation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import com.algaworks.brewer.model.Cliente;

public class ClienteGroupSequenceProvider implements DefaultGroupSequenceProvider<Cliente>{

	@Override
	public List<Class<?>> getValidationGroups(Cliente cliente) {
		List<Class<?>> grupos = new ArrayList<>();
		
		// Adiciono Cliente.class para que todas as anotações (que não tem grupo) sejam validadas
		grupos.add(Cliente.class);
		
		if (isPessoaSelecionada(cliente)) {
			// Retorno o grupo do tipo de pessoa selecionada na tela (a classe de marcação) 
			// e aí vai ser possivel fazer a validação se o conteúdo respeita um CPF ou CNPJ válido. 
			grupos.add(cliente.getTipoPessoa().getGrupo());
		}
		
		return grupos;
	}

	private boolean isPessoaSelecionada(Cliente cliente) {		
		return cliente != null && cliente.getTipoPessoa() != null;
	}

}
