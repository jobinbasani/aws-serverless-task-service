package com.jobinbasani.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.jobinbasani.data.Task;
import com.jobinbasani.service.TaskService;

import javax.inject.Inject;

public class Handler implements RequestHandler<Task, String> {

    @Inject
    TaskService taskService;

    @Override
    public String handleRequest(Task input, Context context) {
        System.out.println("Inputs is "+input.getName());
        taskService.addTask(input);
        return input.getName();
    }
}
