package com.pranshu.hibernate.web;


import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.dto.ProjectsList;
import com.pranshu.hibernate.service.SimpleProjectService;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(VertxExtension.class)
@ExtendWith(MockitoExtension.class)
class WebVerticleTest {

  @Mock private SimpleProjectService projectService;
  @InjectMocks private WebVerticle verticle;

  WebClient client;

  @BeforeEach
  void setup(Vertx vertx, VertxTestContext context) {
    DeploymentOptions options = new DeploymentOptions();
    JsonObject verticleConfig = new JsonObject();
    verticleConfig.put("port", 9090);
    options.setConfig(verticleConfig);

    client = WebClient.create(vertx);

    vertx.deployVerticle(verticle, options)
      .onSuccess(result -> context.completeNow())
      .onFailure(err -> context.failNow(err));
  }

  @Test
  void findProjectByIdExistsTest(Vertx vertx, VertxTestContext context) {
    ProjectDTO project = new ProjectDTO(1, 1, "My Project");
    Mockito.lenient().when(projectService.findProjectById(1)).thenReturn(Future.succeededFuture(Optional.of(project)));
    context.verify(() -> {
      client.getAbs("http://localhost:9090/projects/one/1").send()
        .onFailure(err -> context.failNow(err))
        .onSuccess(result -> {
          int statusCode = result.statusCode();
          Assertions.assertEquals(200, statusCode);
          JsonObject body = result.bodyAsJsonObject();
          Assertions.assertEquals(project.id(), body.getInteger("id"));
          Assertions.assertEquals(project.userId(), body.getInteger("userId"));
          Assertions.assertEquals(project.name(), body.getString("name"));
          context.completeNow();
        });
    });
  }

  @Test
  void findProjectByIdDoesNotExistsTest(Vertx vertx, VertxTestContext context) {
    ProjectDTO project = new ProjectDTO(1, 1, "My Project");
    Mockito.lenient().when(projectService.findProjectById(1)).thenReturn(Future.succeededFuture(Optional.empty()));
    context.verify(() -> {
      client.getAbs("http://localhost:9090/projects/one/1").send()
        .onFailure(err -> context.failNow(err))
        .onSuccess(result -> {
          int statusCode = result.statusCode();
          Assertions.assertEquals(404, statusCode);
          context.completeNow();
        });
    });
  }

  @Test
  void findProjectByUserTest(Vertx vertx, VertxTestContext context) {
    List<ProjectDTO> projects = List.of(
      new ProjectDTO(1, 1, "My Project 1"),
      new ProjectDTO(2, 1, "My Project 2"),
      new ProjectDTO(3, 1, "My Project 3")
    );
    ProjectsList projectsList = new ProjectsList(projects);
    Mockito.when(projectService.findProjectsByUser(1)).thenReturn(Future.succeededFuture(projectsList));
    context.verify(() -> {
      client.getAbs("http://localhost:9090/projects/user/1").send()
        .onFailure(err -> context.failNow(err))
        .onSuccess(result -> {
          int statusCode = result.statusCode();
          Assertions.assertEquals(200, statusCode);
          context.completeNow();
        });
    });
  }

  @Test
  void removeProjectTest(Vertx vertx, VertxTestContext context) {
    Mockito.when(projectService.removeProject(1)).thenReturn(Future.succeededFuture());
    context.verify(() -> {
      client.deleteAbs("http://localhost:9090/projects/1").send()
        .onFailure(err -> context.failNow(err))
        .onSuccess(result -> {
          int statusCode = result.statusCode();
          Assertions.assertEquals(204, statusCode);
          context.completeNow();
        });
    });
  }

  @Test
  void createProjectTest(Vertx vertx, VertxTestContext context) {
    ProjectDTO project = new ProjectDTO(1, 1, "My Project");
    Mockito.when(projectService.createProject(Mockito.any(ProjectDTO.class)))
      .thenReturn(Future.succeededFuture(project));
    context.verify(() -> {
      client.postAbs("http://localhost:9090/projects")
        .sendJsonObject(JsonObject.mapFrom(project))
        .onFailure(err -> context.failNow(err))
        .onSuccess(result -> {
          int statusCode = result.statusCode();
          Assertions.assertEquals(201, statusCode);
          context.completeNow();
        });
    });
  }

  @Test
  void updateProjectTest(Vertx vertx, VertxTestContext context) {
    ProjectDTO project = new ProjectDTO(1, 1, "My Project");
    Mockito.when(projectService.updateProject(Mockito.any(ProjectDTO.class)))
      .thenReturn(Future.succeededFuture(project));
    context.verify(() -> {
      client.putAbs("http://localhost:9090/projects")
        .sendJsonObject(JsonObject.mapFrom(project))
        .onFailure(err -> context.failNow(err))
        .onSuccess(result -> {
          int statusCode = result.statusCode();
          Assertions.assertEquals(200, statusCode);
          context.completeNow();
        });
    });
  }

}
