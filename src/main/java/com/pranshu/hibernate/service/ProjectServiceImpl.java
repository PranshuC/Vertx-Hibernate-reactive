package com.pranshu.hibernate.service;

import com.pranshu.hibernate.auth.Principal;
import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.dto.ProjectsList;
import com.pranshu.hibernate.exception.NotOwnerException;
import com.pranshu.hibernate.repository.ProjectRepository;
import io.vertx.core.Future;

import java.util.Objects;
import java.util.Optional;

public record ProjectServiceImpl(ProjectRepository repository) implements ProjectService {

  @Override
  public Future<ProjectDTO> createProject(ProjectDTO projectDTO) {
    return null;
  }

  @Override
  public Future<ProjectDTO> updateProject(Principal principal, ProjectDTO projectDTO) {
    Integer projectId = projectDTO.id();
    return repository.findProjectById(projectId).compose(result -> {
      if(result.isEmpty()) {
        return Future.failedFuture(new RuntimeException());
      }
      ProjectDTO project = result.get();
      if(Objects.equals(project.userId(), principal.userId())) {
        return repository.updateProject(projectDTO);
      } else {
        return Future.failedFuture(new NotOwnerException());
      }
    });
  }

  @Override
  public Future<Optional<ProjectDTO>> findProjectById(Integer id) {
    return null;
  }

  @Override
  public Future<Void> removeProject(Principal principal, Integer id) {
    return repository.findProjectById(id).compose(result -> {
      if(result.isEmpty()) {
        return Future.failedFuture(new RuntimeException());
      }
      ProjectDTO project = result.get();
      if(Objects.equals(project.userId(), principal.userId())) {
        return repository.removeProject(id);
      } else {
        return Future.failedFuture(new NotOwnerException());
      }
    });
  }

  @Override
  public Future<ProjectsList> findProjectsByUser(Integer userId) {
    return null;
  }
}
