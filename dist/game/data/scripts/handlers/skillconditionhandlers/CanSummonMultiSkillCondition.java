
package handlers.skillconditionhandlers;

import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Sdw
 */
public class CanSummonMultiSkillCondition implements ISkillCondition
{
	private final int _summonPoints;
	
	public CanSummonMultiSkillCondition(StatSet params)
	{
		_summonPoints = params.getInt("summonPoints");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		final PlayerInstance player = caster.getActingPlayer();
		if ((player == null) || player.isSpawnProtected() || player.isTeleportProtected())
		{
			return false;
		}
		
		boolean canSummon = true;
		
		final int servitorSize = player.getServitors().size();
		if ((servitorSize == 1) && (player.getSummonPoints() == 0))
		{
			canSummon = false;
		}
		else if (servitorSize > 4)
		{
			canSummon = false;
		}
		else if (player.isFlyingMounted() || player.isMounted() || player.inObserverMode() || player.isTeleporting())
		{
			canSummon = false;
		}
		else if (player.isInAirShip())
		{
			player.sendPacket(SystemMessageId.A_SERVITOR_CANNOT_BE_SUMMONED_WHILE_ON_AN_AIRSHIP);
			canSummon = false;
		}
		else if ((player.getSummonPoints() + _summonPoints) > player.getMaxSummonPoints())
		{
			final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_CANNOT_USE_THE_S1_SKILL_DUE_TO_INSUFFICIENT_SUMMON_POINTS);
			sm.addSkillName(skill);
			player.sendPacket(sm);
			canSummon = false;
		}
		
		return canSummon;
	}
}
