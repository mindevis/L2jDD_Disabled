
package org.l2jdd.gameserver.model.actor.status;

import org.l2jdd.gameserver.model.actor.instance.StaticObjectInstance;

public class StaticObjectStatus extends CreatureStatus
{
	public StaticObjectStatus(StaticObjectInstance activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public StaticObjectInstance getActiveChar()
	{
		return (StaticObjectInstance) super.getActiveChar();
	}
}
