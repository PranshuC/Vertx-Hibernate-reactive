package com.pranshu.hibernate.repository;

import com.pranshu.hibernate.dto.TaskDTO;
import com.pranshu.hibernate.model.Task;

import java.util.function.Function;

class TaskEntityMapper implements Function<TaskDTO, Task> {

  @Override
  public Task apply(TaskDTO taskDTO) {
    Task task = new Task();
    task.setId(taskDTO.id());
    task.setUserId(taskDTO.userId());
    task.setContent(taskDTO.content());
    task.setCompleted(taskDTO.completed());
    task.setCreatedAt(taskDTO.createdAt());
    return task;
  }
}
