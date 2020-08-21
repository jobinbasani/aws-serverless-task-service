package com.jobinbasani;

import com.jobinbasani.data.Task;
import com.jobinbasani.service.TaskService;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.UUID;

@QuarkusTest
public class TaskServiceTest {

    @Inject
    TaskService taskService;

    @Test
    public void addTaskTest(){
        Task task = new Task();
        task.setTaskId(UUID.randomUUID().toString());
        task.setTaskName("Task Name");
        taskService.addTask(task);
    }
}
