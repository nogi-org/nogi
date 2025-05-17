package kr.co.nogibackend.global.config.logging.p6spy;

import com.p6spy.engine.common.ConnectionInformation;
import com.p6spy.engine.common.PreparedStatementInformation;
import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.spy.P6SpyOptions;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class P6SpyEventListener extends JdbcEventListener {

	public static final ThreadLocal<Integer> dmlCount = ThreadLocal.withInitial(() -> 0);

	@Value("${decorator.datasource.p6spy.enable-logging:true}")
	private boolean enableSQLLogging;

	@Override
	public void onAfterGetConnection(ConnectionInformation connectionInformation, SQLException e) {
		if (enableSQLLogging) {
			P6SpyOptions.getActiveInstance().setLogMessageFormat(P6SpyFormatter.class.getName());
		}
	}

	@Override
	public void onAfterExecuteUpdate(PreparedStatementInformation statementInformation,
			long timeElapsedNanos,
			int rowCount, SQLException e) {
		if (enableSQLLogging && SQLType.isDML(statementInformation.getStatementQuery())) {
			dmlCount.set(dmlCount.get() + 1);
		}
	}

	@Override
	public void onAfterExecuteQuery(PreparedStatementInformation statementInformation,
			long timeElapsedNanos,
			SQLException e) {
		if (enableSQLLogging && SQLType.isDML(statementInformation.getStatementQuery())) {
			dmlCount.set(dmlCount.get() + 1);
		}
	}
}
