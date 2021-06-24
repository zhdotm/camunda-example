package com.example.camunda.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

/**
 * @author zhihao.mao
 */
public class TaskAssignmentListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("assignment监听器");
        delegateTask.getVariables().forEach((s, o) -> {
            System.out.println("===key: " + s);
            System.out.println("===value: " + 0);
        });
        delegateTask.complete();
    }

}
