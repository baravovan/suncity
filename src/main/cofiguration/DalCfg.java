package main.cofiguration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp2.datasources.SharedPoolDataSource;
import org.hibernate.SessionFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.TransactionDefinition;

@Configuration
public class DalCfg {
	
	static Logger log = Logger.getLogger(DalCfg.class.getName());
	
	@Value("${spring.datasource.dbcp2.url}") 
	String dbURL; 
	@Value("${spring.datasource.dbcp2.driver-class-name:com.mysql.cj.jdbc.Driver}") 
	String dbDriver; 
	@Value("${spring.datasource.dbcp2.username}") 
	String dbUser; 
	@Value("${spring.datasource.dbcp2.password}") 
	String dbPwd; 
	@Value("${spring.datasource.dbcp2.max-active}") 
	Integer maxActive; 
	@Value("${spring.datasource.dbcp2.max-age}") 
	Integer maxAge; 
	@Value("${spring.datasource.dbcp2.max-wait:10000}") 
	Integer maxWait; 
	@Value("${spring.datasource.dbcp2.max-idle}") 
	Integer maxIdle; 
	@Value("${spring.datasource.dbcp2.min-idle}") 
	Integer minIdle; 
	@Value("${spring.datasource.dbcp2.initial-size}") 
	Integer initialSize; 
	@Value("${spring.datasource.dbcp2.validation-query:SELECT 1}")
	String validationQuery; 
	@Value("${spring.datasource.dbcp2.test-on-borrow:true}") 
	Boolean testOnBorrow; 
	@Value("${spring.datasource.dbcp2.test-on-return:true}") 
	Boolean testOnReturn; 
	@Value("${spring.datasource.dbcp2.test-while-idle:true}")
	Boolean testWhileIdle;
	
	@Value("${spring.hibernate.hbm2ddl.auto:create-drop}") 
	String hbm2ddlAuto; 
	@Value("${spring.hibernate.dialect:org.hibernate.dialect.MySQL5Dialect}")
	String hibernateDialect;
	@Value("${spring.hibernate.show_sql:false}")
	String hibernateShowSql;
	
	static DataSource ds;
	
	public String getDbURL() 
	{
		return dbURL;
	}

	public String getDbUser() 
	{
		return dbUser;
	}

	public String getDbPwd() 
	{
		return dbPwd;
	}

	public String getHibernateDialect() 
	{
		return hibernateDialect;
	}
	

	@Bean 
	DataSource dataSource() throws ClassNotFoundException {
		
		if(ds == null) {
			ds = createDataSource();
		}
		return ds;
	}
	
	private DataSource createDataSource() throws ClassNotFoundException {
		
		DriverAdapterCPDS adapter = new DriverAdapterCPDS();
        adapter.setMaxIdle( maxIdle );
        adapter.setUrl( dbURL );
        adapter.setUser( dbUser );
        adapter.setPassword( dbPwd );
        adapter.setDriver( dbDriver );
        adapter.setMaxIdle(maxIdle);
        
        SharedPoolDataSource ds = new SharedPoolDataSource();	  
        ds.setConnectionPoolDataSource( adapter );
        
        ds.setDefaultMinIdle(minIdle);
        ds.setDefaultMaxTotal(maxActive);
        ds.setDefaultMaxWaitMillis(maxWait);
        ds.setMaxConnLifetimeMillis(maxAge);
        ds.setDefaultTestOnBorrow(testOnBorrow);
        ds.setDefaultTestOnReturn(testOnReturn);
        ds.setDefaultTestWhileIdle(testWhileIdle);
        ds.setValidationQuery(validationQuery);
        ds.setDefaultMaxWaitMillis(50);
        ds.setDefaultTransactionIsolation(TransactionDefinition.ISOLATION_READ_COMMITTED);
        
        return new TransactionAwareDataSourceProxy(ds);
	}
	
	@Bean
	 public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
	  HibernateTransactionManager txManager = new HibernateTransactionManager();
	  txManager.setSessionFactory(sessionFactory);
	  return txManager;
	}	
	
    @Bean
    public LocalSessionFactoryBean sessionFactory() throws ClassNotFoundException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setPackagesToScan("main.model");
        return sessionFactory;  
    }
    
    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        hibernateProperties.setProperty("hibernate.dialect", hibernateDialect);
        hibernateProperties.setProperty("hibernate.connection.autocommit","true");
        hibernateProperties.setProperty("hibernate.show_sql", hibernateShowSql);
        log.debug("Hibernate properies :" + hibernateProperties.toString());
        return hibernateProperties;
    }
}

