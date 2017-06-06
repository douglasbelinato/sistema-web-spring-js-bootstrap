package com.algaworks.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring4.util.FieldUtils;
import org.thymeleaf.templatemode.TemplateMode;

public class ClassForErrorAttributeTagProcessor extends AbstractAttributeTagProcessor {
	
	private static final String NOME_ATRIBUTO = "classforerror";
	private static final int PRECEDENCIA = 1000;

	public ClassForErrorAttributeTagProcessor(String dialectPrefix) {
		// último parâmetro do construtor pai indica:
		//   true --> remove o novo atributo do Thymeleaf no HTML final
		//   false --> deixa o novo atributo do Thymeleaf no HTML final
		super(TemplateMode.HTML, dialectPrefix, null, false, NOME_ATRIBUTO, true, PRECEDENCIA, true); 
	}

	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
			String attributeValue, IElementTagStructureHandler structureHandler) {
		
		// Lógica do processador para verificar se há erro no atributo
		boolean temErro = FieldUtils.hasErrors(context, attributeValue);
		
		if (temErro) {			
			String classesCssExistentes = tag.getAttributeValue("class"); // obtém as Classes CSS que já existem nessa tag
			structureHandler.setAttribute("class", classesCssExistentes + " has-error"); // incrementa classe has-error do Bootstrap 
		}
		
	}

}
