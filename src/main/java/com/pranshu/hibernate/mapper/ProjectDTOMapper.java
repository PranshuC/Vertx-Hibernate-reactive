package com.pranshu.hibernate.mapper;

import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.model.Project;

import java.util.function.Function;

public class ProjectDTOMapper implements Function<Project, ProjectDTO> {

  @Override
  public ProjectDTO apply(Project project) {
    return new ProjectDTO(project.getId(), project.getUserId(), project.getName());
  }
}
