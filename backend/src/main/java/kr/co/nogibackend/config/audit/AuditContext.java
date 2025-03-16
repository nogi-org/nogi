package kr.co.nogibackend.config.audit;

public class AuditContext {

  private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();
  private static final Long SYSTEM_USER_ID = 0L; // 스케줄링 작업의 기본 사용자 ID

  public static Long getUserId() {
    return userIdHolder.get() != null ? userIdHolder.get() : SYSTEM_USER_ID;
  }

  public static void setUserId(Long userId) {
    userIdHolder.set(userId);
  }

  public static void clear() {
    userIdHolder.remove();
  }
}
