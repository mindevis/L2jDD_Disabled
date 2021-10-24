
package org.l2jdd.gameserver.model.actor.status;

import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Creature;

public class AttackableStatus extends NpcStatus
{
	public AttackableStatus(Attackable activeChar)
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
		if (getActiveChar().isDead())
		{
			return;
		}
		
		if (value > 0)
		{
			if (getActiveChar().isOverhit())
			{
				getActiveChar().setOverhitValues(attacker, value);
			}
			else
			{
				getActiveChar().overhitEnabled(false);
			}
		}
		else
		{
			getActiveChar().overhitEnabled(false);
		}
		
		super.reduceHp(value, attacker, awake, isDOT, isHpConsumption);
		
		if (!getActiveChar().isDead())
		{
			// And the attacker's hit didn't kill the mob, clear the over-hit flag
			getActiveChar().overhitEnabled(false);
		}
	}
	
	@Override
	public boolean setCurrentHp(double newHp, boolean broadcastPacket)
	{
		return super.setCurrentHp(newHp, true);
	}
	
	@Override
	public Attackable getActiveChar()
	{
		return (Attackable) super.getActiveChar();
	}
}