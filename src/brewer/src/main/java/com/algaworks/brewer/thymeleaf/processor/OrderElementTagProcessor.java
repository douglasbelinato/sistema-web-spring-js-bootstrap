package com.algaworks.brewer.thymeleaf.processor;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IAttribute;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class OrderElementTagProcessor extends AbstractElementTagProcessor {

	private static final String NOME_TAG = "order";
	private static final int PRECEDENCIA = 1000;

	public OrderElementTagProcessor(String dialectPrefix) {		
		super(TemplateMode.HTML, dialectPrefix, NOME_TAG, true, null, false, PRECEDENCIA);
	}
	
	@Override
	protected void doProcess(ITemplateContext context, IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		
		IModelFactory modelFactory = context.getModelFactory();
		IModel model = modelFactory.createModel();
		
		IAttribute page = tag.getAttribute("page");
		IAttribute field = tag.getAttribute("field");
		IAttribute text = tag.getAttribute("text");
		
		// No model eu adiciono os elementos HTML que eu quero criar
		model.add(modelFactory.createStandaloneElementTag("th:block", 
				  "th:replace", 
				  String.format("fragments/Ordenacao :: order (%s, %s, %s)", page.getValue(), field.getValue(), text.getValue())));
		
		// Se eu estive adicionando ao modelFactory apenas código HTML puro, que não precisa de processamento da 
		// Template Engine, então eu poderia passar false no segundo parâmetro. Mas como estou adicionando tags 
		// do Thymeleaf, então elas ainda precisam ser processadas pela Template Engine e por isso esse  
		// segundo parâmetro (processable) deve ser true.
		structureHandler.replaceWith(model, true);
		
	}

}