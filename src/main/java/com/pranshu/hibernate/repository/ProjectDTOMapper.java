package com.pranshu.hibernate.repository;

import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.model.Project;

import java.util.function.Function;

class ProjectDTOMapper implements Function<Project, ProjectDTO> {

  @Override
  public ProjectDTO apply(Project project) {
    return new ProjectDTO();
  }
}
