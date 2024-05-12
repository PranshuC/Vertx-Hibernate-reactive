package com.pranshu.hibernate.repository;

import com.pranshu.hibernate.model.Task;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.hibernate.cfg.Configuration;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.hibernate.reactive.stage.Stage;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.CompletionStage;

@ExtendWith(VertxExtension.class)
public class HibernateConfigurationTest {

  @Test
  void initializeHibernateWithCodeTest(Vertx vertx, VertxTestContext context) {
    // 1. Create properties with config data
    Properties hibernateProps = new Properties();
    //URL
    hibernateProps.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/hibernate");
    //credentials
    hibernateProps.put("hibernate.connection.username", "postgres");
    hibernateProps.put("hibernate.connection.password", "password");
    //schema generation
    hibernateProps.put("javax.persistence.schema-generation.database.action", "create");
    //dialect *
    hibernateProps.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect"); // version 9.5

    // 2. Create Hibernate Configuration
    Configuration hibernateConfig = new Configuration();
    hibernateConfig.setProperties(hibernateProps);
    hibernateConfig.addAnnotatedClass(Task.class);

    // 3. Create ServiceRegistry
    ServiceRegistry serviceRegistry = new ReactiveServiceRegistryBuilder()
      .applySettings(hibernateConfig.getProperties()).build();

    // 4. Create SessionFactory
    Stage.SessionFactory sessionFactory = hibernateConfig
      .buildSessionFactory(serviceRegistry).unwrap(Stage.SessionFactory.class);

    // Do something with DB
    Task task = new Task();
    task.setContent("Hello, this is a new task!");
    task.setCompleted(false);
    task.setUserId(1);
    task.setCreatedAt(LocalDateTime.now());

    System.out.println("Task ID before insertion is : " + task.getId());

    CompletionStage<Void> insertionResult = sessionFactory.withTransaction((s, t) -> s.persist(task));
    Future<Void> future = Future.fromCompletionStage(insertionResult);
    context.verify(() -> future.onFailure(err -> context.failNow(err)).onSuccess(r -> {
      System.out.println("Task ID after insertion is : " + task.getId());
      context.completeNow();
    }));
  }
}
