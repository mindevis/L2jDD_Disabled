
package handlers.effecthandlers;

import java.util.logging.Level;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.data.xml.NpcData;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.Spawn;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.DecoyInstance;
import org.l2jdd.gameserver.model.actor.instance.EffectPointInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.actor.templates.NpcTemplate;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.effects.EffectType;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.targets.TargetType;

/**
 * Summon Npc effect implementation.
 * @author Zoey76
 */
public class SummonNpc extends AbstractEffect
{
	private int _despawnDelay;
	private final int _npcId;
	private final int _npcCount;
	private final boolean _randomOffset;
	private final boolean _isSummonSpawn;
	private final boolean _singleInstance; // Only one instance of this NPC is allowed.
	
	public SummonNpc(StatSet params)
	{
		_despawnDelay = params.getInt("despawnDelay", 20000);
		_npcId = params.getInt("npcId", 0);
		_npcCount = params.getInt("npcCount", 1);
		_randomOffset = params.getBoolean("randomOffset", false);
		_isSummonSpawn = params.getBoolean("isSummonSpawn", false);
		_singleInstance = params.getBoolean("singleInstance", false);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.SUMMON_NPC;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		if (!effected.isPlayer() || effected.isAlikeDead() || effected.getActingPlayer().inObserverMode())
		{
			return;
		}
		
		if ((_npcId <= 0) || (_npcCount <= 0))
		{
			LOGGER.warning(SummonNpc.class.getSimpleName() + ": Invalid NPC ID or count skill ID: " + skill.getId());
			return;
		}
		
		final PlayerInstance player = effected.getActingPlayer();
		if (player.isMounted())
		{
			return;
		}
		
		final NpcTemplate npcTemplate = NpcData.getInstance().getTemplate(_npcId);
		if (npcTemplate == null)
		{
			LOGGER.warning(SummonNpc.class.getSimpleName() + ": Spawn of the nonexisting NPC ID: " + _npcId + ", skill ID:" + skill.getId());
			return;
		}
		
		int x = player.getX();
		int y = player.getY();
		int z = player.getZ();
		
		if (skill.getTargetType() == TargetType.GROUND)
		{
			final Location wordPosition = player.getActingPlayer().getCurrentSkillWorldPosition();
			if (wordPosition != null)
			{
				x = wordPosition.getX();
				y = wordPosition.getY();
				z = wordPosition.getZ();
			}
		}
		else
		{
			x = effected.getX();
			y = effected.getY();
			z = effected.getZ();
		}
		
		if (_randomOffset)
		{
			x += (Rnd.nextBoolean() ? Rnd.get(20, 50) : Rnd.get(-50, -20));
			y += (Rnd.nextBoolean() ? Rnd.get(20, 50) : Rnd.get(-50, -20));
		}
		
		switch (npcTemplate.getType())
		{
			case "Decoy":
			{
				final DecoyInstance decoy = new DecoyInstance(npcTemplate, player, _despawnDelay);
				decoy.setCurrentHp(decoy.getMaxHp());
				decoy.setCurrentMp(decoy.getMaxMp());
				decoy.setHeading(player.getHeading());
				decoy.setInstance(player.getInstanceWorld());
				decoy.setSummoner(player);
				decoy.spawnMe(x, y, z);
				break;
			}
			case "EffectPoint": // TODO: Implement proper signet skills.
			{
				final EffectPointInstance effectPoint = new EffectPointInstance(npcTemplate, player);
				effectPoint.setCurrentHp(effectPoint.getMaxHp());
				effectPoint.setCurrentMp(effectPoint.getMaxMp());
				effectPoint.setInvul(true);
				effectPoint.setSummoner(player);
				effectPoint.setTitle(player.getName());
				effectPoint.spawnMe(x, y, z);
				_despawnDelay = effectPoint.getParameters().getInt("despawn_time", 0) * 1000;
				if (_despawnDelay > 0)
				{
					effectPoint.scheduleDespawn(_despawnDelay);
				}
				break;
			}
			default:
			{
				Spawn spawn;
				try
				{
					spawn = new Spawn(npcTemplate);
				}
				catch (Exception e)
				{
					LOGGER.log(Level.WARNING, SummonNpc.class.getSimpleName() + ": Unable to create spawn. " + e.getMessage(), e);
					return;
				}
				
				spawn.setXYZ(x, y, z);
				spawn.setHeading(player.getHeading());
				spawn.stopRespawn();
				
				// If only single instance is allowed, delete previous NPCs.
				if (_singleInstance)
				{
					for (Npc npc : player.getSummonedNpcs())
					{
						if (npc.getId() == _npcId)
						{
							npc.deleteMe();
						}
					}
				}
				
				final Npc npc = spawn.doSpawn(_isSummonSpawn);
				player.addSummonedNpc(npc); // npc.setSummoner(player);
				npc.setName(npcTemplate.getName());
				npc.setTitle(npcTemplate.getName());
				if (_despawnDelay > 0)
				{
					npc.scheduleDespawn(_despawnDelay);
				}
				npc.broadcastInfo();
			}
		}
	}
}
