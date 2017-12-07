var Brewer = Brewer || {};

Brewer.MascaraCpfCnpj = (function () {
	
	function MascaraCpfCnpj() {
		this.radioTipoPessoa = $('.js-radio-tipo-pessoa');
		this.labelCpfCnpj = $('[for=cpfCnpj]');
		this.inputCpfCnpj = $('#cpfCnpj');
	}
	
	MascaraCpfCnpj.prototype.iniciar = function() {
		this.radioTipoPessoa.on('change', onTipoPessoaAlterado.bind(this));
		// .filter(':checked') me retorna um array com as opções selecionadas
		// nesse caso, como é um radio, só vai ter uma posição, por isso já pego [0] direto
		var tipoPessoaSelecionada = this.radioTipoPessoa.filter(':checked')[0];
		
		if (tipoPessoaSelecionada) {
			// passo $(tipoPessoaSelecionada) para transf. em obj do jquery
			// assim dentro da função aplicarMascara consigo usar as funções .data,
			// que são do jquery
			aplicarMascara.call(this, $(tipoPessoaSelecionada));
		}
	}
	
	function onTipoPessoaAlterado(evento) {
		var tipoPessoaSelecionada = $(evento.currentTarget);
		// chamo a função usando call para que o contexto de execução seja o mesmo que está
		// sendo executado nessa função onTipoPessoaAlterado. Assim eu tenho acesso aos
		// campos labelCpfCnpj, inputCpfCnpj e outros, se preciso.
		aplicarMascara.call(this, tipoPessoaSelecionada);
		this.inputCpfCnpj.val('');
	}
	
	function aplicarMascara(tipoPessoaSelecionada) {
		this.labelCpfCnpj.text(tipoPessoaSelecionada.data('documento'));
		this.inputCpfCnpj.mask(tipoPessoaSelecionada.data('mascara'));
		this.inputCpfCnpj.removeAttr('disabled');
	}
	
	return MascaraCpfCnpj;
	
}());

$(function() {
	var mascaraCpfCnpj = new Brewer.MascaraCpfCnpj();
	mascaraCpfCnpj.iniciar();
});