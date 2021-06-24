package com.example.camunda;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Maps;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.impl.BpmnModelInstanceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@SpringBootTest
class CamundaExampleApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Test
    @SneakyThrows
    public void deploy() {
//        String fileAbstractPath = System.getProperty("user.dir") + "/camunda/demo.bpmn";
//        repositoryService.createDeployment().addInputStream("demo.bpmn", new FileInputStream(fileAbstractPath)).deploy().var
        List<Deployment> deploymentList = repositoryService.createDeploymentQuery().list();
        for (Deployment deployment : deploymentList) {
            System.out.println("部署id: " + deployment.getId());
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
            System.out.println("流程定义id: " + processDefinition.getId());
            System.out.println("流程定义key: " + processDefinition.getKey());
            System.out.println("流程定义名称: " + processDefinition.getName());
        }
    }

}
