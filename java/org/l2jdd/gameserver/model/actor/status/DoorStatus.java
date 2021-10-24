
package org.l2jdd.gameserver.model.actor.status;

import org.l2jdd.gameserver.model.actor.instance.DoorInstance;

public class DoorStatus extends CreatureStatus
{
	public DoorStatus(DoorInstance activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public DoorInstance getActiveChar()
	{
		return (DoorInstance) super.getActiveChar();
	}
}
