package com.pranshu.hibernate.mapper;

import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.dto.TaskDTO;
import com.pranshu.hibernate.model.Project;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProjectDTOMapper implements Function<Project, ProjectDTO> {

  @Override
  public ProjectDTO apply(Project project) {
    TaskDTOMapper dtoMapper = new TaskDTOMapper();
    List<TaskDTO> tasks = project.getTasks().stream().map(dtoMapper).collect(Collectors.toList());
    return new ProjectDTO(project.getId(), project.getUserId(), project.getName());
  }
}
