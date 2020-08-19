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
import java.util.Map;

@Data
@ApplicationScoped
public class TaskServiceImpl implements TaskService {

    private final DatabaseConfig databaseConfig;
    private final DynamoDbClient dynamoDbClient;

    public final static String TASK_ID_COL = "taskId";
    public final static String TASK_DESC_COL = "taskDescription";

    @Override
    public void addTask(Task task) {
        System.out.println("table is "+databaseConfig.getTable());

        Map<String, AttributeValue> item = new HashMap<>();
        item.put(TASK_ID_COL, AttributeValue.builder().s("some-id").build());
        item.put(TASK_DESC_COL, AttributeValue.builder().s("task-desc").build());

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(databaseConfig.getTable())
                .item(item)
                .build());

    }
}
