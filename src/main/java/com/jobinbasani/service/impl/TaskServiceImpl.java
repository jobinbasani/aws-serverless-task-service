package com.jobinbasani.service.impl;

import com.jobinbasani.config.DatabaseConfig;
import com.jobinbasani.data.Task;
import com.jobinbasani.service.TaskService;
import lombok.Data;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@ApplicationScoped
public class TaskServiceImpl implements TaskService {

    private final DatabaseConfig databaseConfig;
    private final DynamoDbClient dynamoDbClient;

    public final static String TASK_ID_COL = "taskId";
    public final static String TASK_NAME_COL = "taskName";

    @Override
    public void addTask(Task task) {
        System.out.println("table is "+databaseConfig.getTable());

        Map<String, AttributeValue> item = new HashMap<>();
        item.put(TASK_ID_COL, AttributeValue.builder().s(task.getTaskId()).build());
        item.put(TASK_NAME_COL, AttributeValue.builder().s(task.getTaskName()).build());
        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(databaseConfig.getTable())
                .item(item)
                .build());

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
