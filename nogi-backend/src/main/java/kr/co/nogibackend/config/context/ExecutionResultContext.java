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

  private static final ThreadLocal<List<ProcessingResult>> threadLocal = ThreadLocal.withInitial(
      ArrayList::new);

  public static void addUserErrorResult(String message, Long userId) {
    addResult(false, message, userId);
  }

  public static void addNotionPageErrorResult(String message, Long userId) {
    addResult(false, message, userId);
  }

  public static void addSuccessResult(String message, Long userId) {
    addResult(true, message, userId);
  }

  private static void addResult(boolean success, String message, Long userId) {
    threadLocal.get().add(new ProcessingResult(success, message, userId));
  }

  public static List<ProcessingResult> getResults() {
    return threadLocal.get();
  }

  public static void clear() {
    threadLocal.remove();
  }

  public record ProcessingResult(
      boolean success,
      String message,
      Long userId
  ) {

  }
}
