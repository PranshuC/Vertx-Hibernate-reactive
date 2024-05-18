package com.pranshu.hibernate.guiceApp;

import com.google.inject.AbstractModule;
import com.pranshu.hibernate.model.Project;
import com.pranshu.hibernate.model.Task;
import com.pranshu.hibernate.repository.ProjectRepository;
import com.pranshu.hibernate.repository.ProjectRepositoryImpl;
import com.pranshu.hibernate.service.SimpleProjectService;
import com.pranshu.hibernate.service.SimpleProjectServiceImpl;
import com.pranshu.hibernate.web.WebVerticle;
import org.hibernate.cfg.Configuration;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.hibernate.reactive.stage.Stage;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

class AppModule extends AbstractModule {

  SimpleProjectService projectService;

  AppModule() {
    // 1. Hibernate Configuration
    Properties hibernateProps = new Properties();
    String url = "jdbc:postgresql://localhost:5432/hibernate";
    hibernateProps.put("hibernate.connection.url", url);
    hibernateProps.put("hibernate.connection.username", "postgres");
    hibernateProps.put("hibernate.connection.password", "password");
    hibernateProps.put("javax.persistence.schema-generation.database.action", "drop-and-create");
    hibernateProps.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
    Configuration hibernateConfig = new Configuration();
    hibernateConfig.setProperties(hibernateProps);
    hibernateConfig.addAnnotatedClass(Task.class);
    hibernateConfig.addAnnotatedClass(Project.class);

    // 2. Session factory
    ServiceRegistry serviceRegistry = new ReactiveServiceRegistryBuilder()
      .applySettings(hibernateConfig.getProperties()).build();
    Stage.SessionFactory sessionFactory = hibernateConfig
      .buildSessionFactory(serviceRegistry).unwrap(Stage.SessionFactory.class);

    // 3. Project repository
    ProjectRepository projectRepository = new ProjectRepositoryImpl(sessionFactory);

    // 4. Project service
    this.projectService = new SimpleProjectServiceImpl(projectRepository);
  }

  @Override
  protected void configure() {
    super.configure();
    bind(WebVerticle.class).toInstance(new WebVerticle(this.projectService));
  }

}
