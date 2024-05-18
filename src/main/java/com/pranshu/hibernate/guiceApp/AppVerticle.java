package com.pranshu.hibernate.guiceApp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.pranshu.hibernate.web.WebVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class AppVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    AppModule module = new AppModule();
    Injector injector = Guice.createInjector(module);
    WebVerticle webVerticle = injector.getInstance(WebVerticle.class);

    DeploymentOptions options = new DeploymentOptions();
    JsonObject config = new JsonObject();
    config.put("port", 9090);
    options.setConfig(config);

    vertx.deployVerticle(webVerticle, options)
      .onFailure(err -> startPromise.fail(err))
      .onSuccess(res -> startPromise.complete());
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    AppVerticle appVerticle = new AppVerticle();
    vertx.deployVerticle(appVerticle)
      .onFailure(err -> System.out.println(err.getMessage()))
      .onSuccess(res -> {
        System.out.println("The application is up and running");
      });
  }

}
