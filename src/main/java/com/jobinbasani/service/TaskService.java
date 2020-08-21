package com.jobinbasani.service;

import com.jobinbasani.data.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task addTask(Task task);
    List<Task> getTasks();
    Optional<Task> getTask(String taskId);
    void deleteTask(String taskId);
}
