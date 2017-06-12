package com.algaworks.brewer.config;

import java.math.BigDecimal;
import java.util.Locale;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.number.NumberStyleFormatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.algaworks.brewer.controller.CervejasController;
import com.algaworks.brewer.controller.converter.EstiloConverter;
import com.algaworks.brewer.thymeleaf.BrewerDialect;

import nz.net.ultraq.thymeleaf.LayoutDialect;

/**
 * Ensina ao Spring quais são os Controllers a serem utilizados 
 *
 */
// Observações:
// @Configuration >>> Porque é uma classe de configuração.

// Agora aqui, eu ensino o spring como encontrar as minhas classes
// Posso fazer assim: @ComponentScan("com.algaworks.brewer.controller")
// mas para evitar erros de digitação na String, ou ajudar no refactoring
// do nome de pacotes, posso fazer assim também:
// @ComponentScan(basePackageClasses = { CervejasController.class })

// @EnableWebMvc >>> Habilita o projeto para ser web mvc

// @EnableSpringDataWebSupport >>> Permite uso de funcionalidades específicas do Spring Data junto
// com o Spring MVC, como por exemplo, o Pageable (paginação de  resultados).

// Implemento a interface ApplicationContextAware para que quando a aplicação "subir" eu possa receber
// no método setApplicationContext(...) o arquivo applicationContext como parametro.
@Configuration
@ComponentScan(basePackageClasses = { CervejasController.class })
@EnableWebMvc
@EnableSpringDataWebSupport
public class WebConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	// Também tem @Bean para estar disponível no contexto do Spring.
	// Através do nome "pedido" na URL busca qual a View correspondente
	@Bean
	public ViewResolver viewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}
	
	// É o template engine que consegue processar os arquivos html.
	// Anotando ele com @Bean, o template engine fica disponível dentro do contexto
	// do Spring para ser acessado sempre que for necessário encontrar uma página html.  
	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver());
		
		// Adicionando novo dialeto
		engine.addDialect(new LayoutDialect());
		engine.addDialect(new BrewerDialect());
		
		return engine;
	}
	
	private ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix("classpath:/templates/"); // a partir daqui estão os meus templates
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	/**
	 * Ensina o Spring onde ele deve buscar os recursos estáticos do sistema (CSS, JS, imagens, ...).
	 * Necessário pois não há Controllers para tratar as requisições GET que acessam esses recursos no momento
	 * em que as páginas são carregadas.  
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Para todas as requisições, ou seja, /**, depois do barra "/" qualquer coisa "**" 
		// é pra pesquisar também na pasta "static" que está na nossa aplicação.
		registry.addResourceHandler("/**").addResourceLocations("classpath:static/");
	}
	
	/**
	 * Define e registra os converters.
	 * @return
	 */
	@Bean
	public FormattingConversionService mvcConversionService() { 
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		conversionService.addConverter(new EstiloConverter());
		
		NumberStyleFormatter bigDecimalFormatter = new NumberStyleFormatter("#,##0.00"); // sempre passo o padrão internacional
		conversionService.addFormatterForFieldType(BigDecimal.class, bigDecimalFormatter);
		
		NumberStyleFormatter integerFormatter = new NumberStyleFormatter("#,##0"); // sempre passo o padrão internacional
		conversionService.addFormatterForFieldType(Integer.class, integerFormatter);
		
		return conversionService;
	}
	
	/**
	 * Força configuração de idioma do browser a ser pt-BR.
	 * Quando a internacionalização for aplicada, esse Bean não se fará mais necessário.
	 */
	@Bean
	public LocaleResolver localeResolver() {
		return new FixedLocaleResolver(new Locale("pt","BR"));
	}
	
}
