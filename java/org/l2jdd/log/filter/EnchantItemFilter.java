
package org.l2jdd.log.filter;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class EnchantItemFilter implements Filter
{
	@Override
	public boolean isLoggable(LogRecord record)
	{
		return record.getLoggerName().equalsIgnoreCase("enchant.items");
	}
}
