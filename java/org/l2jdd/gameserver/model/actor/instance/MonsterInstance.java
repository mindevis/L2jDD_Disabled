
package org.l2jdd.gameserver.model.actor.instance;

import org.l2jdd.Config;
import org.l2jdd.gameserver.enums.InstanceType;
import org.l2jdd.gameserver.model.actor.Attackable;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.templates.NpcTemplate;
import org.l2jdd.gameserver.model.effects.EffectFlag;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.util.MinionList;

/**
 * This class manages all Monsters. MonsterInstance:
 * <ul>
 * <li>MinionInstance</li>
 * <li>RaidBossInstance</li>
 * <li>GrandBossInstance</li>
 * </ul>
 */
public class MonsterInstance extends Attackable
{
	protected boolean _enableMinions = true;
	
	private MonsterInstance _master = null;
	private MinionList _minionList = null;
	
	/**
	 * Constructor of MonsterInstance (use Creature and NpcInstance constructor).<br>
	 * <br>
	 * <b><u>Actions</u>:</b>
	 * <ul>
	 * <li>Call the Creature constructor to set the _template of the MonsterInstance (copy skills from template to object and link _calculators to NPC_STD_CALCULATOR)</li>
	 * <li>Set the name of the MonsterInstance</li>
	 * <li>Create a RandomAnimation Task that will be launched after the calculated delay if the server allow it</li>
	 * </ul>
	 * @param template to apply to the NPC
	 */
	public MonsterInstance(NpcTemplate template)
	{
		super(template);
		setInstanceType(InstanceType.MonsterInstance);
		setAutoAttackable(true);
	}
	
	/**
	 * Return True if the attacker is not another MonsterInstance.
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		if (isFakePlayer())
		{
			return isInCombat() || attacker.isMonster() || (getScriptValue() > 0);
		}
		
		// Check if the MonsterInstance target is aggressive
		if (Config.GUARD_ATTACK_AGGRO_MOB && getTemplate().isAggressive() && (attacker instanceof GuardInstance))
		{
			return true;
		}
		
		if (attacker.isMonster())
		{
			return attacker.isFakePlayer();
		}
		
		// Anything considers monsters friendly except Players, Attackables (Guards, Friendly NPC), Traps and EffectPoints.
		if (!attacker.isPlayable() && !attacker.isAttackable() && !(attacker instanceof TrapInstance) && !(attacker instanceof EffectPointInstance))
		{
			return false;
		}
		
		return super.isAutoAttackable(attacker);
	}
	
	/**
	 * Return True if the MonsterInstance is Aggressive (aggroRange > 0).
	 */
	@Override
	public boolean isAggressive()
	{
		return getTemplate().isAggressive() && !isAffected(EffectFlag.PASSIVE);
	}
	
	@Override
	public void onSpawn()
	{
		if (!isTeleporting() && (_master != null))
		{
			setRandomWalking(false);
			setIsRaidMinion(_master.isRaid());
			_master.getMinionList().onMinionSpawn(this);
		}
		
		// dynamic script-based minions spawned here, after all preparations.
		super.onSpawn();
	}
	
	@Override
	public void onTeleported()
	{
		super.onTeleported();
		
		if (hasMinions())
		{
			getMinionList().onMasterTeleported();
		}
	}
	
	@Override
	public boolean deleteMe()
	{
		if (hasMinions())
		{
			getMinionList().onMasterDie(true);
		}
		
		if (_master != null)
		{
			_master.getMinionList().onMinionDie(this, 0);
		}
		
		return super.deleteMe();
	}
	
	@Override
	public MonsterInstance getLeader()
	{
		return _master;
	}
	
	public void setLeader(MonsterInstance leader)
	{
		_master = leader;
	}
	
	public void enableMinions(boolean value)
	{
		_enableMinions = value;
	}
	
	public boolean hasMinions()
	{
		return _minionList != null;
	}
	
	public MinionList getMinionList()
	{
		if (_minionList == null)
		{
			synchronized (this)
			{
				if (_minionList == null)
				{
					_minionList = new MinionList(this);
				}
			}
		}
		return _minionList;
	}
	
	@Override
	public boolean isMonster()
	{
		return true;
	}
	
	/**
	 * @return true if this MonsterInstance (or its master) is registered in WalkingManager
	 */
	@Override
	public boolean isWalker()
	{
		return ((_master == null) ? super.isWalker() : _master.isWalker());
	}
	
	/**
	 * @return {@code true} if this MonsterInstance is not raid minion, master state otherwise.
	 */
	@Override
	public boolean giveRaidCurse()
	{
		return (isRaidMinion() && (_master != null)) ? _master.giveRaidCurse() : super.giveRaidCurse();
	}
	
	@Override
	public synchronized void doCast(Skill skill, ItemInstance item, boolean ctrlPressed, boolean shiftPressed)
	{
		// Might need some exceptions here, but it will prevent the monster buffing player bug.
		if (!skill.isBad() && (getTarget() != null) && getTarget().isPlayer())
		{
			abortAllSkillCasters();
			return;
		}
		super.doCast(skill, item, ctrlPressed, shiftPressed);
	}
}
