package com.jobinbasani.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.jobinbasani.data.Task;
import com.jobinbasani.service.TaskService;
import org.jboss.logging.Logger;

import javax.inject.Inject;

public class Handler implements RequestHandler<Task, String> {

    @Inject
    TaskService taskService;

    private static final Logger LOG = Logger.getLogger(Handler.class);

    @Override
    public String handleRequest(Task input, Context context) {
        LOG.debug("Task name is " + input.getTaskName());
        taskService.addTask(input);
        return input.getTaskName();
    }
}
