
package org.l2jdd.gameserver.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.conditions.ICondition;
import org.l2jdd.gameserver.scripting.ScriptEngineManager;

/**
 * @author Sdw
 */
public class ConditionHandler
{
	private final Map<String, Function<StatSet, ICondition>> _conditionHandlerFactories = new HashMap<>();
	
	public void registerHandler(String name, Function<StatSet, ICondition> handlerFactory)
	{
		_conditionHandlerFactories.put(name, handlerFactory);
	}
	
	public Function<StatSet, ICondition> getHandlerFactory(String name)
	{
		return _conditionHandlerFactories.get(name);
	}
	
	public int size()
	{
		return _conditionHandlerFactories.size();
	}
	
	public void executeScript()
	{
		try
		{
			ScriptEngineManager.getInstance().executeScript(ScriptEngineManager.CONDITION_HANDLER_FILE);
		}
		catch (Exception e)
		{
			throw new Error("Problems while running ConditionMasterHandler", e);
		}
	}
	
	private static final class SingletonHolder
	{
		protected static final ConditionHandler INSTANCE = new ConditionHandler();
	}
	
	public static ConditionHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
}
