
package org.l2jdd.gameserver.model.actor.status;

import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.NpcInstance;

public class FolkStatus extends NpcStatus
{
	public FolkStatus(Npc activeChar)
	{
		super(activeChar);
	}
	
	@Override
	public void reduceHp(double value, Creature attacker)
	{
		reduceHp(value, attacker, true, false, false);
	}
	
	@Override
	public void reduceHp(double value, Creature attacker, boolean awake, boolean isDOT, boolean isHpConsumption)
	{
	}
	
	@Override
	public NpcInstance getActiveChar()
	{
		return (NpcInstance) super.getActiveChar();
	}
}
