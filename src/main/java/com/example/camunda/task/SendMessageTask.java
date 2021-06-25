package com.example.camunda.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

/**
 * @author zhihao.mao
 */

public class SendMessageTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("发送开始消息");
        execution.getProcessInstance().setVariable("message", "开始消息");
    }

}
