package com.pranshu.hibernate.repository;

import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.dto.ProjectsList;
import com.pranshu.hibernate.dto.TaskDTO;
import com.pranshu.hibernate.dto.TasksList;
import com.pranshu.hibernate.model.Project;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

class ProjectDTOMapper implements Function<Project, ProjectDTO> {

  @Override
  public ProjectDTO apply(Project project) {
    TaskDTOMapper dtoMapper = new TaskDTOMapper();
    List<TaskDTO> tasks = project.getTasks().stream().map(dtoMapper).collect(Collectors.toList());
    return new ProjectDTO(project.getId(), project.getUserId(), project.getName());
  }
}
