package com.pranshu.hibernate.mapper;

import com.pranshu.hibernate.dto.TaskDTO;
import com.pranshu.hibernate.model.Task;

import java.util.Optional;
import java.util.function.Function;

public class TaskDTOMapper implements Function<Task, TaskDTO> {

  @Override
  public TaskDTO apply(Task task) {
    //ProjectDTOMapper projectDTOMapper = new ProjectDTOMapper();
    return new TaskDTO(task.getId(), task.getUserId(), task.getContent(), task.isCompleted(), task.getCreatedAt());
      //Optional.of(projectDTOMapper.apply(task.getProject())));
  }
}
