package com.jobinbasani.data;

import lombok.Data;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

import static com.jobinbasani.service.impl.TaskServiceImpl.TASK_ID_COL;
import static com.jobinbasani.service.impl.TaskServiceImpl.TASK_NAME_COL;

@Data
public class Task {
    String taskId;
    String taskName;

    public static Task from(Map<String, AttributeValue> attributes){
        Task task = new Task();
        task.setTaskId(attributes.get(TASK_ID_COL).s());
        task.setTaskName(attributes.get(TASK_NAME_COL).s());
        return task;
    }
}
