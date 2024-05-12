package com.pranshu.hibernate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="tasks")
@NoArgsConstructor
@Getter
@Setter
public class Task {

  @Id @GeneratedValue
  private Integer id;
  private Integer userId;
  private String content;
  private boolean completed;
  private LocalDateTime createdAt;
  @ManyToOne @JoinColumn(name = "projectId", nullable = true)
  private Project project;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Task task)) return false;
    return Objects.equals(id, task.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, content, completed, createdAt);
  }
}
