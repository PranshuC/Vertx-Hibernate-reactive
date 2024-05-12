package com.pranshu.hibernate.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public record TaskDTO(Integer id, Integer userId, String content, boolean completed, LocalDateTime createdAt, Optional<ProjectDTO> project) {
}
