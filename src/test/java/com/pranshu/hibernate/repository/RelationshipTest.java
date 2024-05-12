package com.pranshu.hibernate.repository;

import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.dto.TaskDTO;
import com.pranshu.hibernate.model.Project;
import com.pranshu.hibernate.model.Task;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.hibernate.cfg.Configuration;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.hibernate.reactive.stage.Stage;
import org.hibernate.service.ServiceRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;

@Testcontainers
@ExtendWith(VertxExtension.class)
public class RelationshipTest {

  private static final String DB_NAME = "hibernate";
  private static final String DB_USERNAME = "postgres";
  private static final String DB_PASSWORD = "password";

  @Container
  PostgreSQLContainer container = new PostgreSQLContainer("postgres:16-alpine")
    .withDatabaseName(DB_NAME)
    .withUsername(DB_USERNAME)
    .withPassword(DB_PASSWORD);

  TaskRepositoryImpl taskRepository;
  ProjectRepositoryImpl projectRepository;

  @BeforeEach
  void setup(Vertx vertx, VertxTestContext context) {
    Properties hibernateProps = new Properties();
    String url = "jdbc:postgresql://localhost:" + container.getFirstMappedPort() + "/" + DB_NAME;
    hibernateProps.put("hibernate.connection.url", url);
    hibernateProps.put("hibernate.connection.username", DB_USERNAME);
    hibernateProps.put("hibernate.connection.password", DB_PASSWORD);
    hibernateProps.put("javax.persistence.schema-generation.database.action", "drop-and-create");
    hibernateProps.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
    Configuration hibernateConfig = new Configuration();
    hibernateConfig.setProperties(hibernateProps);
    hibernateConfig.addAnnotatedClass(Task.class);
    hibernateConfig.addAnnotatedClass(Project.class);
    ServiceRegistry serviceRegistry = new ReactiveServiceRegistryBuilder()
      .applySettings(hibernateConfig.getProperties()).build();
    Stage.SessionFactory sessionFactory = hibernateConfig
      .buildSessionFactory(serviceRegistry).unwrap(Stage.SessionFactory.class);
    taskRepository = new TaskRepositoryImpl(sessionFactory);
    projectRepository = new ProjectRepositoryImpl(sessionFactory);
    context.completeNow();
  }

  @Test
  void createRelationshipTest(Vertx vertx, VertxTestContext context) {
    ProjectDTO projectDTO = new ProjectDTO(null, 1, "My Project");
    context.verify(() -> {
      projectRepository.createProject(projectDTO).compose(project -> {
          Assertions.assertEquals(1, project.id());
          TaskDTO taskDTO = new TaskDTO(null, 1, "My Task", false, LocalDateTime.now(), Optional.of(project));
          return taskRepository.createTask(taskDTO);
        }).onFailure(err -> context.failNow(err))
        .onSuccess(result -> {
          Assertions.assertTrue(result.project().isPresent());
          context.completeNow();
        });
    });
  }

}
