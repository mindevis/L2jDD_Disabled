
package org.l2jdd.gameserver.model.html.formatters;

import org.l2jdd.gameserver.model.html.IBypassFormatter;

/**
 * @author UnAfraid
 */
public class BypassParserFormatter implements IBypassFormatter
{
	public static final BypassParserFormatter INSTANCE = new BypassParserFormatter();
	
	@Override
	public String formatBypass(String bypass, int page)
	{
		return bypass + " page=" + page;
	}
}
