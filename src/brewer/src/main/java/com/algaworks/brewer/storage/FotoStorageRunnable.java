package com.algaworks.brewer.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

public class FotoStorageRunnable implements Runnable {
	
	private MultipartFile[] files;
	private DeferredResult<String> resultado;
	
	public FotoStorageRunnable(MultipartFile[] files, DeferredResult<String> resultado) {
		this.files = files;
		this.resultado = resultado;
	}

	@Override
	public void run() {
		System.out.println(">>> Arquivos recebidos: " + files.length);
		// quando faço setResult o Spring entende que eu já posso disparar a thread assíncrona de resposta
		resultado.setResult("OK! Foto recebida!");
	}

}
