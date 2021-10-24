
package handlers.effecthandlers;

import org.l2jdd.gameserver.instancemanager.HandysBlockCheckerManager;
import org.l2jdd.gameserver.model.ArenaParticipantsHolder;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.BlockInstance;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.effects.AbstractEffect;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * Flip Block effect implementation.
 * @author Mobius
 */
public class FlipBlock extends AbstractEffect
{
	public FlipBlock(StatSet params)
	{
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void instant(Creature effector, Creature effected, Skill skill, ItemInstance item)
	{
		final BlockInstance block = effected instanceof BlockInstance ? (BlockInstance) effected : null;
		final PlayerInstance player = effector.isPlayer() ? (PlayerInstance) effector : null;
		if ((block == null) || (player == null))
		{
			return;
		}
		
		final int arena = player.getBlockCheckerArena();
		if (arena != -1)
		{
			final ArenaParticipantsHolder holder = HandysBlockCheckerManager.getInstance().getHolder(arena);
			if (holder == null)
			{
				return;
			}
			
			final int team = holder.getPlayerTeam(player);
			final int color = block.getColorEffect();
			if ((team == 0) && (color == 0x00))
			{
				block.changeColor(player, holder, team);
			}
			else if ((team == 1) && (color == 0x53))
			{
				block.changeColor(player, holder, team);
			}
		}
	}
}