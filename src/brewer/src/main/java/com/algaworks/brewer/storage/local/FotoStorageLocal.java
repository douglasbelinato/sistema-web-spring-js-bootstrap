package com.algaworks.brewer.storage.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.brewer.storage.FotoStorage;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

public class FotoStorageLocal implements FotoStorage {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FotoStorageLocal.class);
	
	private Path local;
	private Path localTemporario;
	
	public FotoStorageLocal() {
		this(FileSystems.getDefault().getPath(System.getenv("USERPROFILE"), ".brewerfotos"));
		// com user.home ele pega usuario com caracteres especiais tbm
		// this(FileSystems.getDefault().getPath(System.getenv("user.home"), ".brewerfotos"));		
	}
	
	public FotoStorageLocal(Path path) {
		this.local = path;
		criarPastas();
	}

	@Override
	public String salvarTemporariamente(MultipartFile[] files) {
		String novoNome = null;
		
		if (files != null && files.length > 0) {
			MultipartFile arquivo = files[0];
			novoNome = renomearArquivo(arquivo.getOriginalFilename());
			try {
				// FileSystems.getDefault().getSeparator() --> para funcionar tanto no Windows quanto no Linux
				File pathTemp = new File(this.localTemporario.toAbsolutePath().toString() + 
										 FileSystems.getDefault().getSeparator() + novoNome);
				arquivo.transferTo(pathTemp);
			} catch (IOException e) {
				throw new RuntimeException("Erro salvando a foto na pasta temporária", e);
			}
		}
		
		return novoNome;
	}
	
	@Override
	public byte[] recuperarFotoTemporaria(String nome) {
		try {
			return Files.readAllBytes(this.localTemporario.resolve(nome));
		} catch (IOException e) {
			throw new RuntimeException("Erro lendo a foto temporária", e);
		}
	}
	
	@Override
	public byte[] recuperar(String nome) {
		try {
			return Files.readAllBytes(this.local.resolve(nome));
		} catch (IOException e) {
			throw new RuntimeException("Erro lendo a foto", e);
		}
	}
	
	@Override
	public void salvar(String foto) {
		try {
			Files.move(this.localTemporario.resolve(foto), this.local.resolve(foto));
		} catch (IOException e) {
			throw new RuntimeException("Erro movendo a foto para destino final", e);
		}
		
		// Gera o Thumbnail da foto
		try {
			Thumbnails.of(this.local.resolve(foto).toString()).size(40, 68).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		} catch (IOException e) {
			throw new RuntimeException("Erro gerando Thumbnail", e);
		}
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
	
	private String renomearArquivo(String nomeOriginal) {
		// Gera UUID para que cada imagem tenha um identificador único a fim de evitar subrescrever fotos com nomes iguais
		String novoNome = UUID.randomUUID().toString() + "_" + nomeOriginal;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Nome original: %s", "Novo nome: %s", nomeOriginal, novoNome));
		}
		return novoNome;
	}

}
