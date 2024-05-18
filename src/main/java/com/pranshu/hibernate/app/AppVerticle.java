package com.pranshu.hibernate.app;

import com.pranshu.hibernate.model.Project;
import com.pranshu.hibernate.model.Task;
import com.pranshu.hibernate.repository.ProjectRepository;
import com.pranshu.hibernate.repository.ProjectRepositoryImpl;
import com.pranshu.hibernate.service.SimpleProjectService;
import com.pranshu.hibernate.service.SimpleProjectServiceImpl;
import com.pranshu.hibernate.web.WebVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.hibernate.cfg.Configuration;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.hibernate.reactive.stage.Stage;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

public class AppVerticle extends AbstractVerticle {

  private final SimpleProjectService projectService;

  public AppVerticle(SimpleProjectService projectService) {
    this.projectService = projectService;
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    WebVerticle webVerticle = new WebVerticle(projectService);

    DeploymentOptions options = new DeploymentOptions();
    JsonObject config = new JsonObject();
    config.put("port", 9090);
    options.setConfig(config);

    vertx.deployVerticle(webVerticle, options)
      .onFailure(err -> startPromise.fail(err))
      .onSuccess(res -> startPromise.complete());
  }

  public static void main(String[] args) {
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
    SimpleProjectService projectService = new SimpleProjectServiceImpl(projectRepository);

    AppVerticle verticle = new AppVerticle(projectService);
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(verticle)
      .onFailure(err -> err.printStackTrace())
      .onSuccess(res -> {
        System.out.println("The app is up and running");
      });
  }
}
