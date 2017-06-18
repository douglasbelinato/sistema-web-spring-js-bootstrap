package com.algaworks.brewer.config.init;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.algaworks.brewer.config.JPAConfig;
import com.algaworks.brewer.config.ServiceConfig;
import com.algaworks.brewer.config.WebConfig;

/**
 * Estou configurando o Dispatcher Servelet do Spring (O Front Controler do Spring)
 * Utiliza os recursos do servlet 3.1 para iniciar a aplicação, pois agora já não
 * preciso mais do web.xml. 
 *
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	/**
	 * Nota: Os beans criados aqui na config. de classes root estão disponíveis para os beans
	 * configurados em getRootConfigClasses().
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { JPAConfig.class, ServiceConfig.class };
	}

	/**
	 * Configuração para determinar quais serão os mapeamentos 
	 * dos Controllers utilizados. Esses mapeamentos foram centralizados
	 * na implementação da clase WebConfig.
	 * Nota: Os beans criados aqui na config. de classes servlets não estão disponíveis para os beans
	 * configurados em getRootConfigClasses()
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class};
	}

	/**
	 *  Configuração para o padrão da URL que será delegado para o dispatcher servlet.
	 *  Com "/" quer dizer que qualquer URL da aplicação passará pelo dispatcher servlet.
	 * 
	 *  Nota: corresponde à configuração do url mappings do arquivo web.xml.
	 */
	@Override	
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	/**
	 * Sobrescrevendo método getServletFilters() para forçar o encoding dos caracteres para UTF-8.
	 * Às vezes, apenas as configurações do workspace já são suficientes para que os caracteres sejam
	 * exibidos corretamente no browser. Mas para evitar riscos dos caracteres ainda aparecerem desconfigurados
	 * em outros SOs, podemos fazer isso como reforço. 
	 */
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);;

		return new Filter[] { characterEncodingFilter };
	}
	
	/**
	 * Aula - 14.3. Upload da foto com Ajax
	 * Configuração para que a aplicação possa receber Content-Type --> multipart/form-data
	 */
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setMultipartConfig(new MultipartConfigElement(""));
	}

}
