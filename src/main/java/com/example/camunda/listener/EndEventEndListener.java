package com.example.camunda.listener;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

@Slf4j
public class EndEventEndListener implements ExecutionListener {
	@Override
	public void notify(DelegateExecution delegateExecution) throws Exception {
		System.out.println("触发 endEvent 的end 触发器");
		String eventName = delegateExecution.getEventName();
		log.info("eventName: {}", eventName);
	}
}
