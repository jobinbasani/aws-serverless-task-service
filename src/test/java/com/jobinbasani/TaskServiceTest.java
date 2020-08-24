package com.jobinbasani;

import com.jobinbasani.data.Task;
import com.jobinbasani.service.TaskService;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@QuarkusTest
@Testcontainers
@TestInstance(PER_CLASS)
public class TaskServiceTest {

    @Inject
    TaskService taskService;
    @ConfigProperty(name="database.create-table-command")
    String createTableCommand;
    @ConfigProperty(name="database.docker-image")
    String dockerImage;
    @ConfigProperty(name="database.docker-command")
    String dockerCommand;
    @ConfigProperty(name="database.container-port")
    int containerPort;
    @ConfigProperty(name="quarkus.dynamodb.endpoint-override")
    String endpointUrl;
    GenericContainer dynamodbContainer;
    private static final Logger LOG = Logger.getLogger(TaskServiceTest.class);

    @BeforeAll
    public void setup() throws IOException, URISyntaxException {
        URI endpoint = new URI(endpointUrl);
        dynamodbContainer = new FixedHostPortGenericContainer(dockerImage)
                .withFixedExposedPort(endpoint.getPort(), containerPort)
                .withCommand(dockerCommand);
        dynamodbContainer.start();
        ProcessBuilder pb = new ProcessBuilder(createTableCommand.split(" "));
        Process process = pb.start();
        String result = new String(process.getInputStream().readAllBytes());
        LOG.debug(result);
    }

    @AfterAll
    public void cleanup() {
        Optional.ofNullable(dynamodbContainer)
                .ifPresent(GenericContainer::stop);
    }

    @Test
    public void addTaskTest(){
        Task task = new Task();
        task.setTaskName("Task Name");
        Task addedTask = taskService.addTask(task);
        assert addedTask.getTaskId() != null;
    }

    @Test
    public void getTaskTest(){
        Task task = new Task();
        task.setTaskName("Task Name");
        Task addedTask = taskService.addTask(task);

        Optional<Task> lookupResult = taskService.getTask(addedTask.getTaskId());
        assert lookupResult.isPresent();
        assert lookupResult.get().getTaskId().equals(addedTask.getTaskId());
        assert lookupResult.get().getTaskName().equals(task.getTaskName());
    }

    @Test
    public void deleteTaskTest(){
        Task task = new Task();
        task.setTaskName("Task Name");
        Task addedTask = taskService.addTask(task);

        taskService.deleteTask(addedTask.getTaskId());

        Optional<Task> lookupResult = taskService.getTask(addedTask.getTaskId());
        assertFalse(lookupResult.isPresent());
    }

}
