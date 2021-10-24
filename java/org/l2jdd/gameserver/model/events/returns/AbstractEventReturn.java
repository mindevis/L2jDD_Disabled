
package org.l2jdd.gameserver.model.events.returns;

/**
 * @author UnAfraid
 */
public abstract class AbstractEventReturn
{
	private final boolean _override;
	private final boolean _abort;
	
	public AbstractEventReturn(boolean override, boolean abort)
	{
		_override = override;
		_abort = abort;
	}
	
	/**
	 * @return {@code true} if return back object must be overridden by this object, {@code false} otherwise.
	 */
	public boolean override()
	{
		return _override;
	}
	
	/**
	 * @return {@code true} if notification has to be terminated, {@code false} otherwise.
	 */
	public boolean abort()
	{
		return _abort;
	}
}
