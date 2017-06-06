package com.algaworks.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class MessageElementTagProcessor extends AbstractElementTagProcessor {

	private static final String NOME_TAG = "message";
	private static final int PRECEDENCIA = 1000;

	public MessageElementTagProcessor(String dialectPrefix) {		
		super(TemplateMode.HTML, dialectPrefix, NOME_TAG, true, null, false, PRECEDENCIA);
	}
	
	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		
		IModelFactory modelFactory = context.getModelFactory();
		IModel model = modelFactory.createModel();
		
		// No model eu adiciono os elementos HTML que eu quero criar
		model.add(modelFactory.createStandaloneElementTag("th:block", "th:replace", "fragments/MensagemSucesso :: message"));
		model.add(modelFactory.createStandaloneElementTag("th:block", "th:replace", "fragments/MensagensErroValidacao :: message"));
		
		// Se eu estive adicionando ao modelFactory apenas código HTML puro, que não precisa de processamento da 
		// Template Engine, então eu poderia passar false no segundo parâmetro. Mas como estou adicionando tags 
		// do Thymeleaf, então elas ainda precisam ser processadas pela Template Engine e por isso esse  
		// segundo parâmetro (processable) deve ser true.
		structureHandler.replaceWith(model, true);
		
	}

}
