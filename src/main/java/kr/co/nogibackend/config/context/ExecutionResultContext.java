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

	public static void addUserErrorResult(String message, Long userId) {
		addResult(false, message, String.valueOf(userId), ProcessingResult.ResultType.USER);
	}

	public static void addNotionPageErrorResult(String message, String notionPageId) {
		addResult(false, message, notionPageId, ProcessingResult.ResultType.NOTION_PAGE);
	}

	public static void addNotionPageSuccessResult(String message, String notionPageId) {
		addResult(true, message, notionPageId, ProcessingResult.ResultType.NOTION_PAGE);
	}

	private static void addResult(boolean success, String message, String key, ProcessingResult.ResultType type) {
		threadLocal.get().add(new ProcessingResult(success, message, key, type));
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
		String key,
		ResultType type
	) {
		public enum ResultType {
			USER(Long.class),
			NOTION_PAGE(String.class);

			private final Class<?> keyType;

			ResultType(Class<?> keyType) {
				this.keyType = keyType;
			}

			public Class<?> getKeyType() {
				return keyType;
			}
		}
	}
}
