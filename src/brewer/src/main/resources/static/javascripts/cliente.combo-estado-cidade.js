var Brewer = Brewer || {};

Brewer.ComboEstado = (function () {
	
	function ComboEstado() {
		this.combo = $('#estado');
		this.emitter = $({}); // criando objeto do jquery para me ajudar a lançar eventos
		this.on = this.emitter.on.bind(this.emitter);
	}
	
	ComboEstado.prototype.iniciar = function() {
		this.combo.on('change', onEstadoAlterado.bind(this));
	}
	
	function onEstadoAlterado() {
		// 2. Dispara evento com nome 'alterado' para mudança de valor no cbo estado
		this.emitter.trigger('alterado', this.combo.val());
	}
	
	return ComboEstado;
	
}());

Brewer.ComboCidade = (function () {
	
	function ComboCidade(comboEstado) {
		this.comboEstado = comboEstado;
		this.combo = $('#cidade');
		this.imgLoading = $('.js-img-loading');
		this.inputHiddenCidadeSelecionada = $('#inputHiddenCidadeSelecionada');
	}
	
	ComboCidade.prototype.iniciar = function() {
		reset.call(this);
		// 4. Ao identificar evento 'alterado', chama função onEstadoAlterado
		this.comboEstado.on('alterado', onEstadoAlterado.bind(this));
		var codigoEstado = this.comboEstado.combo.val();		
		// undefined para o parâmetro evento
		inicializarCidades.call(this, codigoEstado);
	}
	
	function onEstadoAlterado(evento, codigoEstado) {
		this.inputHiddenCidadeSelecionada.val('');
		inicializarCidades.call(this, codigoEstado);
	}
	
	function inicializarCidades(codigoEstado) {
		if (codigoEstado) {
			var resposta = $.ajax({
				url: this.combo.data('url'),
				method: 'GET',
				contentType: 'application/json',
				data: {'estado' : codigoEstado},
				beforeSend: iniciarRequisicao.bind(this), 
				complete: finalizarRequisicao.bind(this)
			});
			resposta.done(onBuscarCidadesFinalizado.bind(this));
		} else {
			reset.call(this);
		}
	}
	
	function onBuscarCidadesFinalizado(cidades) {
		var options = [];
		cidades.forEach(function(cidade) {
			// É melhor adicionar esses options dentro desse array do que incluir direto como código html
			//  através do this.combo.append(...). Esse tipo de operação para incluir como código HTML
			// é lenta.			
			options.push('<option value=' + cidade.codigo + '>' + cidade.nome + '</option>');
		});
		
		// Agora adiciono tudo de uma vez.
		// A operação join junta todos os elementos adicionados no push do array 'options'. 
		this.combo.html(options.join(''));
		this.combo.removeAttr('disabled');
		
		var codigoCidadeSelecionada = this.inputHiddenCidadeSelecionada.val();
		if (codigoCidadeSelecionada) {
			this.combo.val(codigoCidadeSelecionada);
		}
	}
	
	function reset() {
		this.combo.html('<option value="">Selecione a cidade</option>');
		this.combo.val('');
		this.combo.attr('disabled', 'disabled');
	}
	
	function iniciarRequisicao() {
		reset.call(this);
		this.imgLoading.show();
	}
	
	function finalizarRequisicao() {
		this.imgLoading.hide();
	}
	
	return ComboCidade;
	
}());

$(function() {
	var comboEstado = new Brewer.ComboEstado();
	comboEstado.iniciar(); // 1. Cria instância do combo estado
	
	var comboCidade = new Brewer.ComboCidade(comboEstado);
	comboCidade.iniciar(); // 3. Cria instância do combo cidade
});