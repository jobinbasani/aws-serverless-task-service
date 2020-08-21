package com.jobinbasani.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobinbasani.data.Task;
import com.jobinbasani.service.TaskService;
import lombok.SneakyThrows;
import org.jboss.logging.Logger;

import javax.inject.Inject;

public class Handler implements RequestHandler<Task, String> {

    @Inject
    TaskService taskService;

    @Inject
    ObjectMapper mapper;

    private static final Logger LOG = Logger.getLogger(Handler.class);

    @Override
    @SneakyThrows
    public String handleRequest(Task input, Context context) {
        LOG.debug("Task name is " + input.getTaskName());
        Task newTask = taskService.addTask(input);
        return mapper.writeValueAsString(newTask);
    }
}
