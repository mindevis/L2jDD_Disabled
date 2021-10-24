
package org.l2jdd.log.formatter;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.l2jdd.Config;

public class GMAuditFormatter extends Formatter
{
	@Override
	public String format(LogRecord record)
	{
		return record.getMessage() + Config.EOL;
	}
}