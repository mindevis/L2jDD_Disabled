
package org.l2jdd.gameserver.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author UnAfraid
 */
public class VoicedCommandHandler implements IHandler<IVoicedCommandHandler, String>
{
	private final Map<String, IVoicedCommandHandler> _datatable;
	
	protected VoicedCommandHandler()
	{
		_datatable = new HashMap<>();
	}
	
	@Override
	public void registerHandler(IVoicedCommandHandler handler)
	{
		for (String id : handler.getVoicedCommandList())
		{
			_datatable.put(id, handler);
		}
	}
	
	@Override
	public synchronized void removeHandler(IVoicedCommandHandler handler)
	{
		for (String id : handler.getVoicedCommandList())
		{
			_datatable.remove(id);
		}
	}
	
	@Override
	public IVoicedCommandHandler getHandler(String voicedCommand)
	{
		return _datatable.get(voicedCommand.contains(" ") ? voicedCommand.substring(0, voicedCommand.indexOf(' ')) : voicedCommand);
	}
	
	@Override
	public int size()
	{
		return _datatable.size();
	}
	
	public static VoicedCommandHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final VoicedCommandHandler INSTANCE = new VoicedCommandHandler();
	}
}
