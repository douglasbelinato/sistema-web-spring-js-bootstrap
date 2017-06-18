var Brewer = Brewer || {};

//Mistura de module pattern com função construtora
Brewer.EstiloCadastroRapido = (function(){
	
	// função construtora
	function EstiloCadastroRapido() {
		// aqui coloco as inicializações
		// uso .this ao invés de var pra poder usar no objeto inteiro que está sendo criado
		this.modal = $('#modalCadastroRapidoEstilo');
		this.botaoSalvar = this.modal.find('.js-modal-cadastro-estilo-salvar-btn');
		this.form = this.modal.find('form'); // busca o elemento <form>
		this.url = this.form.attr('action');
		this.inputNomeEstilo = $('#nomeEstilo');
		this.containerMensagemErro = $('.js-mensagem-cadastro-rapido-estilo');
	}
	
	EstiloCadastroRapido.prototype.iniciar = function() {
		this.form.on('submit', function(event) {event.preventDefault() }); // evita que ao apertar Enter o formulário do modal seja submetido
		this.modal.on('shown.bs.modal', onModalShow.bind(this)); // evento 'shown.bs.modal' lançado qdo o modal é carregado por completo	
		this.modal.on('hide.bs.modal', onModalClose.bind(this)); //evento lançado qdo o modal é fechado
		this.botaoSalvar.on('click', onBotaoSalvarClick.bind(this));
	}
	
	function onModalShow() {
		this.inputNomeEstilo.focus();
	}
	
	function onModalClose() {
		this.inputNomeEstilo.val('');
		this.containerMensagemErro.addClass('hidden');
		this.form.find('.form-group').removeClass('has-error');
	}
	
	function onBotaoSalvarClick() {
		var nomeEstilo = this.inputNomeEstilo.val().trim();
		//acionando evento $.ajax para disparar chamada AJAX ao servidor
		$.ajax({
			url: this.url,
			method: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ nome: nomeEstilo }),
			error: onErroSalvandoEstilo.bind(this),
			success: onEstiloSalvo.bind(this)
		});
	}
	
	function onErroSalvandoEstilo(obj) {
		var mensagemErro = obj.responseText;
		this.containerMensagemErro.removeClass('hidden');
		this.containerMensagemErro.html('<span>' + mensagemErro + '</span>');
		this.form.find('.form-group').addClass('has-error');		
	}
	
	function onEstiloSalvo(estilo) {
		var comboEstilo = $('#estilo');
		comboEstilo.append('<option value=' + estilo.codigo + '>' + estilo.nome + '</option>');
		comboEstilo.val(estilo.codigo);
		this.modal.modal('hide');
	}
	
	// retorno da função construtora
	return EstiloCadastroRapido;
	
})();

// Aqui faço as coisas acontecerem! Inicio minha execução.
$(function() {
	
	var estiloCadastroRapido = new Brewer.EstiloCadastroRapido();
	estiloCadastroRapido.iniciar();
	
	
	
});