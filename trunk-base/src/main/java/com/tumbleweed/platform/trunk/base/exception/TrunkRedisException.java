/**
 * 
 */
package com.tumbleweed.platform.trunk.base.exception;

import com.tumbleweed.platform.trunk.base.ScriptManager;
import com.tumbleweed.platform.trunk.base.alarm.AbstractMessage.AlarmLevel;

/**
 * @author chao
 */
public class TrunkRedisException extends AbstractException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public TrunkRedisException(Throwable cause) {
		super(cause);
	}

	@Override
	public ErrorDesc getErrorDesc(Throwable cause) {
		String errorCode = "111000";
		String errorMsg = ScriptManager.getErrorDesc(errorCode);
		String customMsg = cause.getMessage();
		AlarmLevel level = AlarmLevel.NORMAL;
		
		if (cause instanceof org.springframework.dao.DataAccessException) {
			errorCode = "111007";
			errorMsg = ScriptManager.getErrorDesc(errorCode);
			customMsg = cause.toString();
			level = AlarmLevel.IMPORTANT;
		}
		
		return new ErrorDesc(errorCode, errorMsg, customMsg, level);
	}
}
