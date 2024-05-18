package com.pranshu.hibernate.repository;

import com.pranshu.hibernate.dto.TaskDTO;
import com.pranshu.hibernate.dto.TasksList;
import com.pranshu.hibernate.mapper.TaskDTOMapper;
import com.pranshu.hibernate.mapper.TaskEntityMapper;
import com.pranshu.hibernate.model.Task;
import io.vertx.core.Future;
import org.hibernate.reactive.stage.Stage;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public record TaskRepositoryImpl (Stage.SessionFactory sessionFactory) implements TaskRepository {

  @Override
  public Future<TaskDTO> createTask(TaskDTO task) {
    TaskEntityMapper entityMapper = new TaskEntityMapper();
    Task entity = entityMapper.apply(task);
    CompletionStage<Void> result = sessionFactory.withTransaction((s, t) -> s.persist(entity));
    TaskDTOMapper dtoMapper = new TaskDTOMapper();
    Future<TaskDTO> future = Future.fromCompletionStage(result).map(v -> dtoMapper.apply(entity));
    return future;
  }

  @Override
  public Future<TaskDTO> updateTask(TaskDTO task) {
    CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
    CriteriaUpdate<Task> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Task.class);
    Root<Task> root = criteriaUpdate.from(Task.class);
    Predicate predicate = criteriaBuilder.equal(root.get("id"), task.id());
    criteriaUpdate.set("content", task.content());
    criteriaUpdate.set("completed", task.completed());
    criteriaUpdate.where(predicate);
    CompletionStage<Integer> result = sessionFactory.withTransaction((s,t) -> s.createQuery(criteriaUpdate).executeUpdate());
    Future<TaskDTO> future = Future.fromCompletionStage(result).map(r -> task);
    return future;
  }

  @Override
  public Future<Void> removeTask(Integer id) {
    CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
    CriteriaDelete<Task> criteriaDelete = criteriaBuilder.createCriteriaDelete(Task.class);
    Root<Task> root = criteriaDelete.from(Task.class);
    Predicate predicate = criteriaBuilder.equal(root.get("id"), id);
    criteriaDelete.where(predicate);
    // SQL query : DELETE FROM tasks WHERE id = [id]
    CompletionStage<Integer> result = sessionFactory.withTransaction((s,t) -> s.createQuery(criteriaDelete).executeUpdate());
    Future<Void> future = Future.fromCompletionStage(result).compose(r -> Future.succeededFuture());
    return future;
  }

  @Override
  public Future<Optional<TaskDTO>> findTaskById(Integer id) {
    TaskDTOMapper dtoMapper = new TaskDTOMapper();
    CompletionStage<Task> result = sessionFactory().withTransaction((s,t) -> s.find(Task.class, id));
    Future<Optional<TaskDTO>> future = Future.fromCompletionStage(result)
      .map(r -> Optional.ofNullable(r))
      .map(r -> r.map(dtoMapper));
    return future;
  }

  @Override
  public Future<TasksList> findTasksByUser(Integer userId) {
    TaskDTOMapper dtoMapper = new TaskDTOMapper();
    CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
    CriteriaQuery<Task> criteriaQuery = criteriaBuilder.createQuery(Task.class);
    Root<Task> root = criteriaQuery.from(Task.class);
    Predicate predicate = criteriaBuilder.equal(root.get("userId"), userId);
    criteriaQuery.where(predicate);
    CompletionStage<List<Task>> result = sessionFactory.withTransaction((s, t) -> s.createQuery(criteriaQuery).getResultList());
    Future<TasksList> future = Future.fromCompletionStage(result)
      .map(list -> list.stream().map(dtoMapper).collect(Collectors.toList()))
      .map(list -> new TasksList(list));
    return future;
  }
}
