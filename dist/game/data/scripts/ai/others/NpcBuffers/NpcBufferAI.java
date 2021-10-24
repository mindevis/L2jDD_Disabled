
package ai.others.NpcBuffers;

import java.util.logging.Logger;

import org.l2jdd.commons.concurrent.ThreadPool;
import org.l2jdd.gameserver.data.xml.SkillData;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.BuffInfo;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author UnAfraid
 */
class NpcBufferAI implements Runnable
{
	private static final Logger LOGGER = Logger.getLogger(NpcBufferAI.class.getName());
	private final Npc _npc;
	private final NpcBufferSkillData _skillData;
	
	protected NpcBufferAI(Npc npc, NpcBufferSkillData skill)
	{
		_npc = npc;
		_skillData = skill;
	}
	
	private Skill getSkill(PlayerInstance player)
	{
		if (_skillData.getScaleToLevel() < 1)
		{
			return _skillData.getSkill();
		}
		
		final BuffInfo currentBuff = player.getEffectList().getBuffInfoBySkillId(_skillData.getSkill().getId());
		if (currentBuff != null)
		{
			int level = currentBuff.getSkill().getLevel();
			if (_skillData.getScaleToLevel() > level)
			{
				level++;
			}
			
			final Skill skill = SkillData.getInstance().getSkill(_skillData.getSkill().getId(), level);
			if (skill == null)
			{
				LOGGER.warning("Requested non existing skill level: " + level + " for id: " + _skillData.getSkill().getId());
			}
			return skill;
		}
		
		return _skillData.getSkill();
	}
	
	@Override
	public void run()
	{
		if ((_npc == null) || !_npc.isSpawned() || _npc.isDecayed() || _npc.isDead() || (_skillData == null) || (_skillData.getSkill() == null))
		{
			return;
		}
		
		if ((_npc.getSummoner() == null) || !_npc.getSummoner().isPlayer())
		{
			return;
		}
		
		final PlayerInstance player = _npc.getSummoner().getActingPlayer();
		final Skill skill = getSkill(player);
		if (skill == null)
		{
			return;
		}
		
		_npc.doCast(skill);
		
		ThreadPool.schedule(this, skill.getReuseDelay());
	}
}