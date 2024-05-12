package com.pranshu.hibernate.repository;

import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.dto.ProjectsList;
import io.vertx.core.Future;
import org.hibernate.reactive.stage.Stage;

import java.util.Optional;

public record ProjectRepositoryImpl(Stage.SessionFactory sessionFactory) implements ProjectRepository {

  @Override
  public Future<ProjectDTO> createProject(ProjectDTO projectDTO) {
    return null;
  }

  @Override
  public Future<ProjectDTO> updateProject(ProjectDTO projectDTO) {
    return null;
  }

  @Override
  public Future<Optional<ProjectDTO>> findProjectById(Integer id) {
    return null;
  }

  @Override
  public Future<Void> removeProject(Integer id) {
    return null;
  }

  @Override
  public Future<ProjectsList> findProjectsByUser(Integer userId) {
    return null;
  }

}
