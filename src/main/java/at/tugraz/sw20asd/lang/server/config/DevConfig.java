package at.tugraz.sw20asd.lang.server.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Profile("dev")
@EnableJpaRepositories(basePackages = {"at.tugraz.sw20asd.lang.server.repositories"})
@PropertySource("classpath:persistence-h2.properties")
@EnableTransactionManagement
public class DevConfig {

    private final Environment _env;

    @Autowired
    public DevConfig(Environment env) {
        _env = env;
    }

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(_env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(_env.getProperty("jdbc.url"));
        dataSource.setUsername(_env.getProperty("jdbc.user"));
        dataSource.setPassword(_env.getProperty("jdbc.pass"));

        return dataSource;
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("at.tugraz.sw20asd.lang.server.domain");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    JpaTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.dialect", _env.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.show_sql", _env.getProperty("hibernate.show_sql"));
        if (_env.containsProperty("hibernate.hbm2ddl.auto")) {
            hibernateProperties.setProperty("hibernate.hbm2ddl.auto", _env.getProperty("hibernate.hbm2ddl.auto"));
        }

        return hibernateProperties;
    }
}
