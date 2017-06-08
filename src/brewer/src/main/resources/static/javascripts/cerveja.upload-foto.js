var Brewer = Brewer || {};

//Mistura de module pattern com função construtora
Brewer.UploadFoto = (function() {
	
	// função construtora
	function UploadFoto() {
		this.inputNomeFoto = $('input[name=foto]');
		this.inputContentType = $('input[name=contentType]');

		this.htmlFotoCervejaTemplate = $('#foto-cerveja').html();
		this.template = Handlebars.compile(this.htmlFotoCervejaTemplate);
		//var htmlFotoCerveja = template({nomeFoto: resposta.nome});

		this.containerFotoCerveja = $('.js-container-foto-cerveja');

		this.uploadDrop = $('#upload-drop');
	}
	
	// Prototype para a função de inicialização
	UploadFoto.prototype.iniciar = function() {
		// Criando objeto para configurar o UIKit
		var settings = {
				type: 'json',
				filelimit: 1,
				allow: '*.(jpg|jpeg|png)',
				action: this.containerFotoCerveja.data('url-fotos'),
				complete: onUploadCompleto.bind(this)
				
		}
		
		// Iniciando configuração para o UIKit através do objeto acima
		UIkit.uploadSelect($('#upload-select'), settings);
		UIkit.uploadDrop(this.uploadDrop, settings);
	}
	
	function onUploadCompleto(resposta) {
		this.inputNomeFoto.val(resposta.nome);
		this.inputContentType.val(resposta.contentType);
		
		this.uploadDrop.addClass('hidden');
		var htmlFotoCerveja = this.template({nomeFoto: resposta.nome});
		this.containerFotoCerveja.append(htmlFotoCerveja);
		
		// coloco esse evento onclick só agora porque somente agora eu tenho
		// o htmlFotoCerveja inserido no HTML da página (depois que eu fiz append 
		// dele no HTML na linha de cima). Por isso não declarei ele antes.
		$('.js-remove-foto').on('click', onRemoverFoto.bind(this));			
	}
	
	function onRemoverFoto() {
		$('.js-foto-cerveja').remove();
		this.uploadDrop.removeClass('hidden');
		this.inputNomeFoto.val('');
		this.inputContentType.val('');
	}
	
	// retorno da função construtora
	return UploadFoto;
	
})();

// Aqui faço as coisas acontecerem! Inicio minha execução.
$(function() {
	var uploadFoto = new Brewer.UploadFoto();
	uploadFoto.iniciar();
});