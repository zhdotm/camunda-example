package com.example.camunda;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

@Slf4j
@SpringBootTest
class CamundaExampleApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AuthorizationService authorizationService;

    @Test
    @SneakyThrows
    public void deploy() {
        String fileAbstractPath = System.getProperty("user.dir") + "/camunda/send_receive_demo.bpmn";
        repositoryService.createDeployment().addInputStream("send_receive_demo.bpmn", new FileInputStream(fileAbstractPath)).tenantId("tenant:id:11111111").deploy();
        List<Deployment> deploymentList = repositoryService.createDeploymentQuery().list();
        for (Deployment deployment : deploymentList) {
            System.out.println("部署id: " + deployment.getId());
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
            System.out.println("流程定义id: " + processDefinition.getId());
            System.out.println("流程定义key: " + processDefinition.getKey());
            System.out.println("流程定义名称: " + processDefinition.getName());
        }
    }

    @Test
    public void startProcess() {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            log.info("process id: {}, key: {}, name: {}, description: {}", processDefinition.getId(), processDefinition.getKey(), processDefinition.getName(), processDefinition.getDescription());
            HashMap<String, Object> map = Maps.newHashMap();
            map.put("name", "lisi");
            runtimeService.startProcessInstanceById(processDefinition.getId(), map);
        }
    }

    @Test
    public void startProcessByKey() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().active().latestVersion().processDefinitionKey("Process_05mr1a9").singleResult();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), new HashMap<String, Object>() {{
            put("param1", "value1");
        }});
    }

    @Test
    public void sendMessage() {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().active().processDefinitionKey("Process_05mr1a9").singleResult();
        runtimeService.createMessageCorrelation("message").setVariable("param2", "value2").correlate();
    }

    @Test
    public void startReceiveMessageProcess() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().active().latestVersion().processDefinitionName("接受任务案例").singleResult();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), new HashMap<String, Object>() {{
            put("name", "zhangsan");
        }});
    }

    @Test
    public void queryProcessDefinition() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().tenantIdIn("tenant:id:11111111").list();
        for (ProcessDefinition processDefinition : list) {
            System.out.println("id: " + processDefinition.getId());
            System.out.println("name: " + processDefinition.getName());
            System.out.println("key: " + processDefinition.getKey());
            System.out.println("description: " + processDefinition.getDescription());
        }
    }

    @Test
    public void startServiceTaskProcess() {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().processDefinitionKeyLike("%Process_service_task%").list();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            log.info("服务任务: id [{}]", processDefinition.getId());
            log.info("服务任务: name [{}]", processDefinition.getName());
            log.info("服务任务: key [{}]", processDefinition.getKey());
            log.info("服务任务: description [{}]", processDefinition.getDescription());
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(), "business:key:11111111111", new HashMap<String, Object>() {{
                //用于给网关判断是给谁的服务任务
                put("userName", "zhangsan");
                //给张三的一些参数
                put("message", "回家吃饭");
            }});

        }
    }

    @Test
    public void task() {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinition.getId()).list();
            for (ProcessInstance processInstance : processInstanceList) {
                Task task = taskService.createTaskQuery().active().processInstanceId(processInstance.getId()).singleResult();
                log.info("task id: {}", task.getId());
                log.info("task name: {}", task.getName());
                log.info("task assignee: {}", task.getAssignee());
                log.info("task description: {}", task.getDescription());
                taskService.complete(task.getId());
            }

        }
    }

    @Test
    public void deleteProcessInstance() {
        for (ProcessInstance processInstance : runtimeService.createProcessInstanceQuery().list()) {
            runtimeService.deleteProcessInstance(processInstance.getId(), "删除理由");
        }
    }

    @Test
    public void receiveMessageTask() {
        for (Task task : taskService.createTaskQuery().active().list()) {
            runtimeService.signal(task.getExecutionId());
        }
    }

    @Test
    public void doReceiveMessage() {

    }

}
