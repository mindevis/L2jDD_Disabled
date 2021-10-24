
package handlers.skillconditionhandlers;

import java.util.List;

import org.l2jdd.gameserver.data.xml.ClanHallData;
import org.l2jdd.gameserver.model.StatSet;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.residences.ClanHall;
import org.l2jdd.gameserver.model.skills.ISkillCondition;
import org.l2jdd.gameserver.model.skills.Skill;

/**
 * @author Sdw
 */
public class OpCheckResidenceSkillCondition implements ISkillCondition
{
	private final List<Integer> _residencesId;
	private final boolean _isWithin;
	
	public OpCheckResidenceSkillCondition(StatSet params)
	{
		_residencesId = params.getList("residencesId", Integer.class);
		_isWithin = params.getBoolean("isWithin");
	}
	
	@Override
	public boolean canUse(Creature caster, Skill skill, WorldObject target)
	{
		if (caster.isPlayer())
		{
			final Clan clan = caster.getActingPlayer().getClan();
			if (clan != null)
			{
				final ClanHall clanHall = ClanHallData.getInstance().getClanHallByClan(clan);
				if (clanHall != null)
				{
					return _isWithin ? _residencesId.contains(clanHall.getResidenceId()) : !_residencesId.contains(clanHall.getResidenceId());
				}
			}
		}
		return false;
	}
}
