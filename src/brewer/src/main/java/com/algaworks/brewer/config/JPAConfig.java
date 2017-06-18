package com.algaworks.brewer.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.algaworks.brewer.model.Cerveja;
import com.algaworks.brewer.repository.Cervejas;

/**
 * @Configuration por ser uma classe configuração.
 * @EnableJpaRepositories para habilitar a criação de repositórios. 
 * 		basePackageClasses --> para localizar onde estão as classes de repositórios.
 * 		enableDefaultTransactions --> para que as classes de repositório não utilizem 
 * 									  o controle de transação default do Spring. Pelo default,
 * 									  até as consultas abrem (begin) e encerram (commit) transação. 
 * @EnableTransactionManagement para informar que o controle de transação será customizado, e não o default do Spring.   
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = Cervejas.class, enableDefaultTransactions = false)
@EnableTransactionManagement
public class JPAConfig {
	
	/**
	 * Obtém a instância para o data source
	 * @return
	 */
	@Bean
	public DataSource dataSource() {
		JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
		dataSourceLookup.setResourceRef(true); // true para ele procurar dentro do container JEE (dentro do TomCat)
		return dataSourceLookup.getDataSource("jdbc/brewerDB");
	}
	
	/**
	 * Obtém a instância para a implementação JPA que estamos usando no projeto
	 * @return
	 */
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);
		adapter.setShowSql(false); // vou configurar no log4j a exibição das queries SQL
		adapter.setGenerateDdl(false); // não gerar alterações no banco de acordo com as anotações dos modelos, já que estamos usando FlyWay
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
		return adapter;
	}
	
	/**
	 * Obtém a instância para o EntityManager
	 */
	@Bean
	public EntityManagerFactory entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setJpaVendorAdapter(jpaVendorAdapter);
		factory.setPackagesToScan(Cerveja.class.getPackage().getName()); // passo nome do pacote através das classes, ao invés do nome dos pacotes direto como String - Facilita refatoração
		factory.afterPropertiesSet();
		return factory.getObject();		
	}
	
	/**
	 * Obtém a configuração para o gerenciamento de transações
	 */
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}
}
