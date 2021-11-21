/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.l2jdd.gameserver.model.skills.effects;

import org.l2jdd.gameserver.model.Effect;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;
import org.l2jdd.gameserver.model.clan.Clan;
import org.l2jdd.gameserver.model.skills.Env;
import org.l2jdd.gameserver.network.SystemMessageId;
import org.l2jdd.gameserver.network.serverpackets.SystemMessage;

/**
 * @author ZaKaX (Ghost @ L2D)
 */
public class EffectClanGate extends Effect
{
	public EffectClanGate(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public void onStart()
	{
		getEffected().startAbnormalEffect(Creature.ABNORMAL_EFFECT_MAGIC_CIRCLE);
		if (getEffected() instanceof PlayerInstance)
		{
			final Clan clan = ((PlayerInstance) getEffected()).getClan();
			if (clan != null)
			{
				clan.broadcastToOtherOnlineMembers(new SystemMessage(SystemMessageId.COURT_MAGICIAN_THE_PORTAL_HAS_BEEN_CREATED), ((PlayerInstance) getEffected()));
			}
		}
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopAbnormalEffect(Creature.ABNORMAL_EFFECT_MAGIC_CIRCLE);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.CLAN_GATE;
	}
}