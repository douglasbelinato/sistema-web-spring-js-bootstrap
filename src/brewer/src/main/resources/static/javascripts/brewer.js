// Objeto global Brewer (namespace)- se não existir, então cria o objeto.
var Brewer = Brewer || {};

// Mistura de module pattern com função construtora
Brewer.MaskMoney = (function() {
	
	// função construtora
	function MaskMoney() {
		// aqui coloco as inicializações
		// uso .this ao invés de var pra poder usar no objeto inteiro que está sendo criado
		this.decimal = $('.js-decimal');
		this.plain = $('.js-plain');
	}
	
	MaskMoney.prototype.enable = function() {
		this.decimal.maskMoney({ decimal: ',', thousands: '.' });
		this.plain.maskMoney({ precision: 0, thousands: '.' });
	}
	
	return MaskMoney;
})();

$(function() {
	var maskMoney = new Brewer.MaskMoney();
	maskMoney.enable();
});