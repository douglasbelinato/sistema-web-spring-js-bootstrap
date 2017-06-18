package com.algaworks.brewer.service.event.cerveja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.algaworks.brewer.storage.FotoStorage;

@Component
public class CervejaListener {
	
	@Autowired
	private FotoStorage fotoStorage;
	
	// Seguindo o padrão Java Bean ficaria assim.
	// @EventListener(condition = "#evento.foto")
	// Mas aí eu precisaria ter o método getFoto() ou isFoto() no lugar de temFoto() em CervejaSalvaEvent.
	@EventListener(condition = "#evento.temFoto()")
	public void cervejaSalva(CervejaSalvaEvent evento) {
		fotoStorage.salvar(evento.getCerveja().getFoto());
	}

}
