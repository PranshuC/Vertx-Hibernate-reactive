package com.pranshu.hibernate.mapper;

import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.model.Project;

import java.util.function.Function;

public class ProjectEntityMapper implements Function<ProjectDTO, Project> {

  @Override
  public Project apply(ProjectDTO projectDTO) {
    Project project = new Project();
    project.setId(projectDTO.id());
    project.setUserId(projectDTO.userId());
    project.setName(projectDTO.name());
    return project;
  }
}
