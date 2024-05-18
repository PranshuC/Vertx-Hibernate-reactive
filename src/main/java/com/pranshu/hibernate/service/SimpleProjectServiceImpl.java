package com.pranshu.hibernate.service;

import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.dto.ProjectsList;
import com.pranshu.hibernate.repository.ProjectRepository;
import io.vertx.core.Future;

import java.util.Optional;

public record SimpleProjectServiceImpl(ProjectRepository repository) implements SimpleProjectService {

  @Override
  public Future<ProjectDTO> createProject(ProjectDTO projectDTO) {
    return repository().createProject(projectDTO);
  }

  @Override
  public Future<ProjectDTO> updateProject(ProjectDTO projectDTO) {
    return repository().updateProject(projectDTO);
  }

  @Override
  public Future<Optional<ProjectDTO>> findProjectById(Integer id) {
    return repository().findProjectById(id);
  }

  @Override
  public Future<Void> removeProject(Integer id) {
    return repository().removeProject(id);
  }

  @Override
  public Future<ProjectsList> findProjectsByUser(Integer userId) {
    return repository().findProjectsByUser(userId);
  }

}
