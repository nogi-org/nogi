package kr.co.nogibackend.config.context;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/*
  Package Name : kr.co.nogibackend.config.context
  File Name    : ExecutionResultContext
  Author       : won taek oh
  Created Date : 25. 2. 9.
  Description  :
 */
@Component
public class ExecutionResultContext {
	private static final ThreadLocal<List<ProcessingResult>> threadLocal = ThreadLocal.withInitial(ArrayList::new);

	public static void addErrorResult(String message, String notionDatabaseId) {
		addResult(false, message, notionDatabaseId);
	}

	public static void addSuccessResult(String message, String notionDatabaseId) {
		addResult(true, message, notionDatabaseId);
	}

	private static void addResult(boolean success, String message, String notionDatabaseId) {
		threadLocal.get().add(new ProcessingResult(success, message, notionDatabaseId));
	}

	public List<ProcessingResult> getAllResults() {
		return threadLocal.get();
	}

	public void clear() {
		threadLocal.remove();
	}

	public record ProcessingResult(
		boolean success,
		String message,
		String notionDatabaseId
	) {

	}
}
