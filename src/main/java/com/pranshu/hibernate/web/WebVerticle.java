package com.pranshu.hibernate.web;

import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.service.SimpleProjectService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class WebVerticle extends AbstractVerticle {

  private final SimpleProjectService projectService;

  public WebVerticle(SimpleProjectService projectService) {
    this.projectService = projectService;
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);

    router.route("/*").handler(BodyHandler.create());

    router.post("/projects").handler(context -> {
      JsonObject body = context.getBodyAsJson();
      Integer userId = body.getInteger("userId");
      String name = body.getString("name");
      ProjectDTO payload = new ProjectDTO(null, userId, name);
      projectService.createProject(payload)
        .onSuccess(result -> {
          JsonObject responseBody = JsonObject.mapFrom(result);
          context.response().setStatusCode(201).end(responseBody.encode());
        })
        .onFailure(err -> context.response().setStatusCode(500).end());
    });

    router.put("/projects").handler(context -> {
      JsonObject body = context.getBodyAsJson();
      Integer id = body.getInteger("id");
      Integer userId = body.getInteger("userId");
      String name = body.getString("name");
      ProjectDTO payload = new ProjectDTO(id, userId, name);
      projectService.updateProject(payload)
        .onSuccess(result -> {
          JsonObject responseBody = JsonObject.mapFrom(result);
          context.response().setStatusCode(200).end(responseBody.encode());
        })
        .onFailure(err -> context.response().setStatusCode(500).end());
    });

    router.get("/projects/one/:id").handler(context -> {
      Integer id = Integer.valueOf(context.pathParam("id"));
      projectService.findProjectById(id)
        .onSuccess(result -> {
          if(result.isPresent()) {
              JsonObject body = JsonObject.mapFrom(result.get());
              context.response().setStatusCode(200).end(body.encode());
          } else {
            context.response().setStatusCode(404).end();
          }
        })
        .onFailure(err -> context.response().setStatusCode(500).end());
    });

    router.get("/projects/user/:userId").handler(context -> {
      Integer userId = Integer.valueOf(context.pathParam("userId"));
      projectService.findProjectsByUser(userId)
        .onSuccess(result -> {
          JsonObject body = JsonObject.mapFrom(result);
          context.response().setStatusCode(200).end(body.encode());
        })
        .onFailure(err -> context.response().setStatusCode(500).end());
    });

    router.delete("/projects/:id").handler(context -> {
      Integer id = Integer.valueOf(context.pathParam("id"));
      projectService.removeProject(id)
        .onSuccess(result -> context.response().setStatusCode(204).end())
        .onFailure(err -> context.response().setStatusCode(500).end());
    });

    JsonObject config = config();
    Integer port = config.getInteger("port");
    server.requestHandler(router).listen(port).onSuccess(result -> startPromise.complete())
      .onFailure(err -> startPromise.fail(err));
  }

  /*public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    WebVerticle verticle = new WebVerticle();
    DeploymentOptions options = new DeploymentOptions();
    JsonObject config = new JsonObject();
    config.put("port", 9090);
    options.setConfig(config);
    vertx.deployVerticle(verticle, options)
      .onSuccess(id -> System.out.println(id))
      .onFailure(err -> System.out.println(err.getMessage()));
  }*/

}
