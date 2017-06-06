package com.algaworks.brewer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.dto.FotoDTO;
import com.algaworks.brewer.storage.FotoStorageRunnable;

// Auala 14.3. Upload da foto com Ajax


/*
 * @RestController => Uso no lugar de @Controller, pois é uma anotação de 
 * 					  convenção que já anota @Controller e @ResponseBody. Para
 * 					  chamadas AJAX, já me ajuda porque não preciso anota
 * 					  @ResponseBody no retorno dos meus métodos. 
 * 
*/
@RestController
@RequestMapping("/fotos")
public class FotosController {
	
	// @PostMapping - nova anotação a partir do Spring 4.3 
	// Spring < 4.3 deve-se continuar usando @RequestMapping(method = RequestMethod.POST)
	@PostMapping
	public DeferredResult<FotoDTO> upload(@RequestParam("files[]") MultipartFile[] files) {
		
		// Aula - 14.4. Melhorando a disponibilidade da aplicação - retorno assíncrono
		// Para retorno assíncrono o tipo de retorno do método é DeferredResult<T>
		DeferredResult<FotoDTO> resultado = new DeferredResult<>();
		
		Thread thread = new Thread(new FotoStorageRunnable(files, resultado));
		thread.start();
		
		return resultado;
	}

}
