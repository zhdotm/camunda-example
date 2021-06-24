package com.example.camunda;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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

	@Test
	@SneakyThrows
	public void deploy() {
		String fileAbstractPath = System.getProperty("user.dir") + "/camunda/demo.bpmn";
		repositoryService.createDeployment().addInputStream("demo.bpmn", new FileInputStream(fileAbstractPath)).deploy();
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
			map.put("name","lisi");
			runtimeService.startProcessInstanceById(processDefinition.getId(), map);
		}
	}

	@Test
	public void task(){
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
		for (ProcessDefinition processDefinition : processDefinitionList) {
			List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinition.getId()).list();
			for (ProcessInstance processInstance : processInstanceList) {
				Task task = taskService.createTaskQuery().active().processInstanceId(processInstance.getId()).singleResult();
				log.info("task id: {}",task.getId());
				log.info("task name: {}",task.getName());
				log.info("task assignee: {}",task.getAssignee());
				log.info("task description: {}",task.getDescription());
				taskService.complete(task.getId());
			}

		}
	}

}
