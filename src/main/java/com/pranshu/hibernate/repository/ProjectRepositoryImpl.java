package com.pranshu.hibernate.repository;

import com.pranshu.hibernate.dto.ProjectDTO;
import com.pranshu.hibernate.dto.ProjectsList;
import com.pranshu.hibernate.mapper.ProjectDTOMapper;
import com.pranshu.hibernate.mapper.ProjectEntityMapper;
import com.pranshu.hibernate.model.Project;
import io.vertx.core.Future;
import org.hibernate.reactive.stage.Stage;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public record ProjectRepositoryImpl(Stage.SessionFactory sessionFactory) implements ProjectRepository {

  @Override
  public Future<ProjectDTO> createProject(ProjectDTO projectDTO) {
    ProjectEntityMapper entityMapper = new ProjectEntityMapper();
    Project entity = entityMapper.apply(projectDTO);
    CompletionStage<Void> result = sessionFactory.withTransaction((s, t) -> s.persist(entity));
    ProjectDTOMapper dtoMapper = new ProjectDTOMapper();
    Future<ProjectDTO> future = Future.fromCompletionStage(result).map(v -> dtoMapper.apply(entity));
    return future;
  }

  @Override
  public Future<ProjectDTO> updateProject(ProjectDTO projectDTO) {
    CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
    CriteriaUpdate<Project> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Project.class);
    Root<Project> root = criteriaUpdate.from(Project.class);
    Predicate predicate = criteriaBuilder.equal(root.get("id"), projectDTO.id());
    // Cannot update : id - unique identifier, userId - ownership detail
    criteriaUpdate.set("name", projectDTO.name());
    criteriaUpdate.where(predicate);
    CompletionStage<Integer> result = sessionFactory.withTransaction((s,t) -> s.createQuery(criteriaUpdate).executeUpdate());
    Future<ProjectDTO> future = Future.fromCompletionStage(result).map(r -> projectDTO);
    return future;
  }

  @Override
  public Future<Optional<ProjectDTO>> findProjectById(Integer id) {
    ProjectDTOMapper dtoMapper = new ProjectDTOMapper();
    CompletionStage<Project> result = sessionFactory.withTransaction((s, t) -> s.find(Project.class, id));
    Future<Optional<ProjectDTO>> future = Future.fromCompletionStage(result)
      .map(r -> Optional.ofNullable(r))
      .map(r -> r.map(dtoMapper));
    return future;
  }

  @Override
  public Future<ProjectsList> findProjectsByUser(Integer userId) {
    ProjectDTOMapper dtoMapper = new ProjectDTOMapper();
    CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
    CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
    Root<Project> root = criteriaQuery.from(Project.class);
    Predicate predicate = criteriaBuilder.equal(root.get("userId"), userId);
    criteriaQuery.where(predicate);
    CompletionStage<List<Project>> result = sessionFactory.withTransaction((s, t) -> s.createQuery(criteriaQuery).getResultList());
    Future<ProjectsList> future = Future.fromCompletionStage(result)
      .map(list -> list.stream().map(dtoMapper).collect(Collectors.toList()))
      .map(list -> new ProjectsList(list));
    return future;
  }

  @Override
  public Future<Void> removeProject(Integer id) {
    CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
    CriteriaDelete<Project> criteriaDelete = criteriaBuilder.createCriteriaDelete(Project.class);
    Root<Project> root = criteriaDelete.from(Project.class);
    Predicate predicate = criteriaBuilder.equal(root.get("id"), id);
    criteriaDelete.where(predicate);
    // SQL query : DELETE FROM projects WHERE id = [id]
    CompletionStage<Integer> result = sessionFactory.withTransaction((s,t) -> s.createQuery(criteriaDelete).executeUpdate());
    Future<Void> future = Future.fromCompletionStage(result).compose(r -> Future.succeededFuture());
    return future;
  }

}
