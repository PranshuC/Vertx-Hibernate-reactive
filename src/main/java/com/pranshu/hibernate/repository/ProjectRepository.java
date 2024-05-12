package com.pranshu.hibernate.repository;

import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.dto.ProjectsList;
import io.vertx.core.Future;

import java.util.Optional;

public interface ProjectRepository {

  Future<ProjectDTO> createProject (ProjectDTO projectDTO);
  Future<ProjectDTO> updateProject (ProjectDTO projectDTO);
  Future<Optional<ProjectDTO>> findProjectById (Integer id);
  Future<Void> removeProject (Integer id);
  Future<ProjectsList> findProjectsByUser (Integer userId);

}
