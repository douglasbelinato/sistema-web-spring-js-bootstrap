package com.algaworks.brewer.storage.local;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.storage.FotoStorage;

public class FotoStorageLocal implements FotoStorage {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FotoStorageLocal.class);
	
	private Path local;
	private Path localTemporario;
	
	public FotoStorageLocal() {
		this(FileSystems.getDefault().getPath(System.getenv("USERPROFILE"), ".brewerfotos"));		
	}
	
	public FotoStorageLocal(Path path) {
		this.local = path;
		criarPastas();
	}

	private void criarPastas() {
		try {
			Files.createDirectories(this.local);
			this.localTemporario = FileSystems.getDefault().getPath(this.local.toString(), "temp");
			Files.createDirectories(this.localTemporario);
			// Files.createTempDirectory(prefix, attrs) --> createTempDirector cria a foto em um local  
			// temporário e já apaga na sequência (cada SO tem um tempo para apagar os arq. temp.). Como 
			// aqui a foto só vai ser apagada depois que houver uma interação com o usuário para confirmar 
			// a gravação da cerveja, então eu vou usar um diretório comum e tratá-lo como temporário. 
			// Depois eu apago a foto de lá.
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Pastas criadas com sucesso para salvar a foto");
				LOGGER.debug("Pasta default: " + this.local.toAbsolutePath());
				LOGGER.debug("Pasta temporária: " + this.localTemporario.toAbsolutePath());
			}
			
		} catch (IOException e) {
			throw new RuntimeException("Erro criando pasta para salvar foto", e);
		}		
	}

	@Override
	public void salvarTemporariamente(MultipartFile[] files) {
		System.out.println("Salvando a foto temporariamente");
	}

}
