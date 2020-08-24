package com.jobinbasani.service.impl;

import com.jobinbasani.config.DatabaseConfig;
import com.jobinbasani.data.Task;
import com.jobinbasani.service.TaskService;
import lombok.Data;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@Data
@ApplicationScoped
public class TaskServiceImpl implements TaskService {

    private final DatabaseConfig databaseConfig;
    private final DynamoDbClient dynamoDbClient;

    public final static String TASK_ID_COL = "taskId";
    public final static String TASK_NAME_COL = "taskName";

    private static final Logger LOG = Logger.getLogger(TaskServiceImpl.class);

    @Override
    public Task addTask(Task request) {
        LOG.debug("Table is " + databaseConfig.getTable());
        Task newTask = new Task();
        newTask.setTaskId(UUID.randomUUID().toString());
        newTask.setTaskName(request.getTaskName());
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(TASK_ID_COL, AttributeValue.builder().s(newTask.getTaskId()).build());
        item.put(TASK_NAME_COL, AttributeValue.builder().s(newTask.getTaskName()).build());
        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(databaseConfig.getTable())
                .item(item)
                .build());
        return newTask;
    }

    @Override
    public List<Task> getTasks() {

        return null;
    }

    @Override
    public Optional<Task> getTask(String taskId) {

        return Optional.empty();
    }

    @Override
    public void deleteTask(String taskId) {

    }
}
