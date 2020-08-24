package com.jobinbasani.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobinbasani.data.Task;
import com.jobinbasani.data.TaskRequest;
import com.jobinbasani.enums.REQUEST_TYPE;
import com.jobinbasani.service.TaskService;
import lombok.SneakyThrows;
import org.jboss.logging.Logger;

import javax.inject.Inject;

public class Handler implements RequestHandler<TaskRequest, String> {

    @Inject
    TaskService taskService;

    @Inject
    ObjectMapper mapper;

    private static final Logger LOG = Logger.getLogger(Handler.class);

    @Override
    @SneakyThrows
    public String handleRequest(TaskRequest input, Context context) {
        switch (parseRequest(input.getAction())) {
            case CREATE_TASK:
                return addTask(input);
            case UNKNOWN:
                return null;
        }
        return null;
    }

    private String addTask(TaskRequest input) throws JsonProcessingException {
        Task newTask = taskService.addTask(input);
        return mapper.writeValueAsString(newTask);
    }

    private REQUEST_TYPE parseRequest(String requestType) {
        try {
            return REQUEST_TYPE.valueOf(requestType);
        } catch (Exception e) {
            return REQUEST_TYPE.UNKNOWN;
        }
    }
}
