package com.algaworks.brewer.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.dto.FotoDTO;

public class FotoStorageRunnable implements Runnable {
	
	private MultipartFile[] files;
	private DeferredResult<FotoDTO> resultado;
	private FotoStorage fotoStorage;
	
	public FotoStorageRunnable(MultipartFile[] files, DeferredResult<FotoDTO> resultado, FotoStorage fotoStorage) {
		this.files = files;
		this.resultado = resultado;
		this.fotoStorage = fotoStorage;
	}

	@Override
	public void run() {
		fotoStorage.salvarTemporariamente(files);
		
		String nomeFoto = files[0].getOriginalFilename();
		String contentType = files[0].getContentType();
		
		// quando faço setResult o Spring entende que eu já posso disparar a thread assíncrona de resposta
		resultado.setResult(new FotoDTO(nomeFoto, contentType));
	}

}
