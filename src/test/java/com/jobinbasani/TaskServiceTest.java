package com.jobinbasani;

import com.jobinbasani.config.TestDatabaseConfig;
import com.jobinbasani.data.Task;
import com.jobinbasani.data.TaskRequest;
import com.jobinbasani.enums.REQUEST_TYPE;
import com.jobinbasani.service.TaskService;
import io.quarkus.amazon.lambda.test.LambdaClient;
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
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static com.jobinbasani.service.impl.TaskServiceImpl.TASK_ID_COL;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@QuarkusTest
@Testcontainers
@TestInstance(PER_CLASS)
public class TaskServiceTest {

    @Inject
    TaskService taskService;
    @ConfigProperty(name="quarkus.dynamodb.endpoint-override")
    String endpointUrl;
    @Inject
    DynamoDbClient dynamoDbClient;
    @Inject
    TestDatabaseConfig testDatabaseConfig;
    GenericContainer dynamodbContainer;
    private static final Logger LOG = Logger.getLogger(TaskServiceTest.class);

    @BeforeAll
    public void setup() throws URISyntaxException {
        URI endpoint = new URI(endpointUrl);
        dynamodbContainer = new FixedHostPortGenericContainer(testDatabaseConfig.getDockerImage())
                .withFixedExposedPort(endpoint.getPort(), testDatabaseConfig.getContainerPort())
                .withCommand(testDatabaseConfig.getDockerCommand());
        dynamodbContainer.start();
        CreateTableResponse response = dynamoDbClient.createTable(CreateTableRequest.builder()
                .tableName(testDatabaseConfig.getTable())
                .attributeDefinitions(AttributeDefinition.builder()
                        .attributeName(TASK_ID_COL)
                        .attributeType(ScalarAttributeType.S)
                        .build())
                .keySchema(KeySchemaElement.builder()
                        .attributeName(TASK_ID_COL)
                        .keyType(KeyType.HASH)
                        .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(testDatabaseConfig.getReadCapacityUnits())
                        .writeCapacityUnits(testDatabaseConfig.getWriteCapacityUnits())
                        .build())
                .build());
        LOG.debug("Table creation status : "+response.tableDescription().tableStatus().toString());
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

    @Test
    public void getAllTasks(){
        taskService.getTasks()
                .stream()
                .map(Task::getTaskId)
                .forEach(taskService::deleteTask);

        Task task = new Task();
        task.setTaskName("Task 1");
        taskService.addTask(task);
        task.setTaskName("Task 2");
        taskService.addTask(task);

        List<Task> allTasks = taskService.getTasks();
        assertTrue(allTasks.size() == 2);
    }

    @Test
    public void testCreateAction(){
        String taskName = "New Task";
        TaskRequest request = new TaskRequest();
        request.setAction(REQUEST_TYPE.CREATE_TASK.toString());
        request.setTaskName(taskName);
        String outputJson = LambdaClient.invoke(String.class, request);
        assertThatJson(outputJson)
                .inPath("$.taskName")
                .isEqualTo(taskName);
    }

}
