package com.pranshu.hibernate.repository;

import com.pranshu.hibernate.dto.TaskDTO;
import com.pranshu.hibernate.model.Task;

import java.util.function.Function;

class TaskDTOMapper implements Function<Task, TaskDTO> {

  @Override
  public TaskDTO apply(Task task) {
    return new TaskDTO(task. getId(), task.getUserId(), task.getContent(), task.isCompleted(), task.getCreatedAt());
  }
}
