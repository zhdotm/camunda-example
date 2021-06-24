package com.example.camunda.listener;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

public class TaskExecutionStartListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        System.out.println("ExecutionStart监听器");
        execution.getVariables().forEach((s, o) -> {
            System.out.println("===key: " + s);
            System.out.println("===value: " + o);
        });
    }

}
