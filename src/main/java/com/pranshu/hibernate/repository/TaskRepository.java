package com.pranshu.hibernate.repository;

import com.pranshu.hibernate.dto.TaskDTO;
import com.pranshu.hibernate.dto.TasksList;
import io.vertx.core.Future;

import java.util.Optional;

public interface TaskRepository {

  Future<TaskDTO> createTask(TaskDTO task);

  Future<TaskDTO> updateTask(TaskDTO task);

  Future<Void> removeTask(Integer id);

  Future<Optional<TaskDTO>> findTaskById(Integer id);

  Future<TasksList> findTasksByUser (Integer userId);
}
