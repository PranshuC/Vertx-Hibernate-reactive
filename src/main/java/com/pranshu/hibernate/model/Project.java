package com.pranshu.hibernate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="projects")
@NoArgsConstructor
@Getter
@Setter
public class Project {

  @Id @GeneratedValue
  private Integer id;
  private Integer userId;
  private String name;
  @OneToMany(mappedBy = "project")
  private List<Task> tasks;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Project project)) return false;
    return Objects.equals(id, project.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, name, tasks);
  }
}
