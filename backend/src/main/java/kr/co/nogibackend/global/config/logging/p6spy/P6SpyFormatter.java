package kr.co.nogibackend.global.config.logging.p6spy;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import java.util.Locale;
import java.util.Map;
import kr.co.nogibackend.global.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class P6SpyFormatter implements MessageFormattingStrategy {

	// ANSI 색상 코드 정의
	private static final String ANSI_GREEN = "\u001B[32m"; // 초록색 (DDL)
	private static final String ANSI_BLUE = "\u001B[34m"; // 파란색 (DML)
	private static final String ANSI_RED = "\u001B[31m"; // 빨간색 (Execution Time 초과)
	private static final String ANSI_RESET = "\u001B[0m"; // 색상 초기화

	private static final long LIMIT_TIME = 1000; // ✅ 1초(1000ms) 이상 → 빨간색

	private static String formatByCommand(String category) {
		return StringUtils.replaceVariables("""
				Execute Command :
				
				\t${category}
				
				----------------------------------------------------------------------------------------------------
				""", Map.of("category", category));
	}

	@Override
	public String formatMessage(int connectionId, String now, long elapsed, String category,
			String prepared,
			String sql,
			String url) {
		if (sql.trim().isEmpty()) {
			return formatByCommand(category);
		}
		return formatBySql(sql, category) + getExecutionTimeMessage(elapsed);
	}

	private String formatBySql(String sql, String category) {
		if (isStatementDDL(sql, category)) {
			return StringUtils.replaceVariables("""
					Execute DDL :
					${color}${formattedSql}${ansiReset}
					""", Map.of(
					"color", ANSI_GREEN,
					"formattedSql", FormatStyle.DDL.getFormatter().format(sql),
					"ansiReset", ANSI_RESET
			));
		}

		return StringUtils.replaceVariables("""
				Execute DML :
				${color}${formattedSql}${ansiReset}
				""", Map.of(
				"color", ANSI_GREEN,
				"formattedSql", FormatStyle.BASIC.getFormatter().format(sql),
				"ansiReset", ANSI_RESET
		));
	}

	private String getExecutionTimeMessage(long elapsed) {
		String elapsedTimeColor = elapsed > LIMIT_TIME ? ANSI_RED : ANSI_BLUE;

		return StringUtils.replaceVariables("""
				
				Execution Time: ${color}${elapsed} ms${reset}
				----------------------------------------------------------------------------------------------------
				""", Map.of(
				"color", elapsedTimeColor,
				"elapsed", String.valueOf(elapsed),
				"reset", ANSI_RESET
		));
	}

	private boolean isStatementDDL(String sql, String category) {
		return isStatement(category) && SQLType.isDDL(sql.trim().toLowerCase(Locale.ROOT));
	}

	private boolean isStatement(String category) {
		return Category.STATEMENT.getName().equals(category);
	}
}
