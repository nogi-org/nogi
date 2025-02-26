package kr.co.nogibackend.config.logging.p6spy;

import java.util.Arrays;

/**
 * ✅ SQL 키워드를 관리하는 Inner Enum
 */
public enum SQLType {
  // ✅ DDL 키워드
  CREATE("create", SQLType.Category.DDL),
  ALTER("alter", SQLType.Category.DDL),
  DROP("drop", SQLType.Category.DDL),
  COMMENT("comment", SQLType.Category.DDL),

  // ✅ DML 키워드
  INSERT("insert", SQLType.Category.DML),
  UPDATE("update", SQLType.Category.DML),
  DELETE("delete", SQLType.Category.DML),
  SELECT("select", SQLType.Category.DML);

  private final String keyword;
  private final SQLType.Category category;

  SQLType(String keyword, SQLType.Category category) {
    this.keyword = keyword;
    this.category = category;
  }

  public static boolean isDDL(String sql) {
    return matches(sql, SQLType.Category.DDL);
  }

  public static boolean isDML(String sql) {
    return matches(sql, SQLType.Category.DML);
  }

  private static boolean matches(String sql, SQLType.Category category) {
    return Arrays.stream(values())
        .anyMatch(type -> type.category == category && sql.startsWith(type.keyword));
  }

  private enum Category {DDL, DML}
}
