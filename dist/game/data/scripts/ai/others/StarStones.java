
package ai.others;

import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.network.SystemMessageId;

import ai.AbstractNpcAI;

/**
 * Star Stones AI.
 * @author Gigiikun
 */
public class StarStones extends AbstractNpcAI
{
	// NPCs
	// @formatter:off
	private static final int[] MOBS =
	{
		18684, 18685, 18686, 18687, 18688, 18689, 18690, 18691, 18692
	};
	// @formatter:on
	// Misc
	private static final int COLLECTION_RATE = 1;
	
	public StarStones()
	{
		addSkillSeeId(MOBS);
	}
	
	@Override
	public String onSkillSee(Npc npc, PlayerInstance caster, Skill skill, WorldObject[] targets, boolean isSummon)
	{
		if (skill.getId() == 932)
		{
			int itemId = 0;
			
			switch (npc.getId())
			{
				case 18684:
				case 18685:
				case 18686:
				{
					// give Red item
					itemId = 14009;
					break;
				}
				case 18687:
				case 18688:
				case 18689:
				{
					// give Blue item
					itemId = 14010;
					break;
				}
				case 18690:
				case 18691:
				case 18692:
				{
					// give Green item
					itemId = 14011;
					break;
				}
				default:
				{
					// unknown npc!
					return super.onSkillSee(npc, caster, skill, targets, isSummon);
				}
			}
			if (getRandom(100) < 33)
			{
				caster.sendPacket(SystemMessageId.YOUR_COLLECTION_HAS_SUCCEEDED);
				caster.addItem("StarStone", itemId, getRandom(COLLECTION_RATE + 1, 2 * COLLECTION_RATE), null, true);
			}
			else if (((skill.getLevel() == 1) && (getRandom(100) < 15)) || ((skill.getLevel() == 2) && (getRandom(100) < 50)) || ((skill.getLevel() == 3) && (getRandom(100) < 75)))
			{
				caster.sendPacket(SystemMessageId.YOUR_COLLECTION_HAS_SUCCEEDED);
				caster.addItem("StarStone", itemId, getRandom(1, COLLECTION_RATE), null, true);
			}
			else
			{
				caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_FAILED);
			}
			npc.deleteMe();
		}
		return super.onSkillSee(npc, caster, skill, targets, isSummon);
	}
	
	public static void main(String[] args)
	{
		new StarStones();
	}
}
