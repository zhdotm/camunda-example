package com.example.camunda.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.Map;

/**
 * @author zhihao.mao
 */

@Slf4j
public class ZsServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Map<String, Object> variables = execution.getVariables();
        log.info("========张三的服务任务获取到的所有参数========: {}", variables);
        log.info("========张三的服务任务执行相关操作========");
    }

}
