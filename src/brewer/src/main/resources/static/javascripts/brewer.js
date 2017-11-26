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

Brewer.MaskPhoneNumber = (function() {
	
	function MaskPhoneNumber() {
		this.inputPhoneNumber = $('.js-phone-number');		
	}
	      
	MaskPhoneNumber.prototype.enable = function() {
		var maskBehavior = function (val) {
			return val.replace(/\D/g, '').length === 11 ? '(00) 00000-0000' : '(00) 0000-00009';
		};
		
		var objOptions = {
				onKeyPress: function(val, e, field, options) {
					field.mask(maskBehavior.apply({}, arguments), options);
					}
		};
		
		this.inputPhoneNumber.mask(maskBehavior, objOptions);
	}
	
	return MaskPhoneNumber;
}());

$(function() {
	var maskMoney = new Brewer.MaskMoney();
	maskMoney.enable();
	
	var maskPhoneNumber = new Brewer.MaskPhoneNumber();
	maskPhoneNumber.enable();
});