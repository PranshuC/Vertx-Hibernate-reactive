package com.pranshu.hibernate.dto;

import com.pranshu.hibernate.model.Task;

import java.util.List;

public record ProjectDTO(Integer id, Integer userId, String name, List<TaskDTO> tasks) {
}
