
package org.l2jdd.gameserver.model.actor.instance;

import org.l2jdd.Config;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.model.actor.templates.NpcTemplate;
import org.l2jdd.gameserver.network.serverpackets.PlaySound;

/**
 * This class manages all RaidBoss.<br>
 * In a group mob, there are one master called RaidBoss and several slaves called Minions.
 */
public class RaidBossInstance extends MonsterInstance
{
	private boolean _useRaidCurse = true;
	
	/**
	 * Constructor of RaidBossInstance (use Creature and NpcInstance constructor).<br>
	 * <br>
	 * <b><u>Actions</u>:</b>
	 * <ul>
	 * <li>Call the Creature constructor to set the _template of the RaidBossInstance (copy skills from template to object and link _calculators to NPC_STD_CALCULATOR)</li>
	 * <li>Set the name of the RaidBossInstance</li>
	 * <li>Create a RandomAnimation Task that will be launched after the calculated delay if the server allow it</li>
	 * </ul>
	 * @param template to apply to the NPC
	 */
	public RaidBossInstance(NpcTemplate template)
	{
		super(template);
		setInstanceType(InstanceType.RaidBossInstance);
		setIsRaid(true);
		setLethalable(false);
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		setRandomWalking(false);
		broadcastPacket(new PlaySound(1, getParameters().getString("RaidSpawnMusic", "Rm01_A"), 0, 0, 0, 0, 0));
	}
	
	@Override
	public int getVitalityPoints(int level, double exp, boolean isBoss)
	{
		return -super.getVitalityPoints(level, exp, isBoss);
	}
	
	@Override
	public boolean useVitalityRate()
	{
		return Config.RAIDBOSS_USE_VITALITY;
	}
	
	public void setUseRaidCurse(boolean value)
	{
		_useRaidCurse = value;
	}
	
	@Override
	public boolean giveRaidCurse()
	{
		return _useRaidCurse;
	}
}
