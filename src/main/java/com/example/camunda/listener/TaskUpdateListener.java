package com.example.camunda.listener;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;

public class TaskUpdateListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("Update监听器");
        delegateTask.getVariables().forEach((s, o) -> {
            System.out.println("===key: " + s);
            System.out.println("===value: " + o);
        });

    }
}
