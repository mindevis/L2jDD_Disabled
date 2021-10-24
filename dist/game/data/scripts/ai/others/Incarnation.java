
package ai.others;

import org.l2jdd.gameserver.ai.CtrlIntention;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.events.EventType;
import org.l2jdd.gameserver.model.events.ListenerRegisterType;
import org.l2jdd.gameserver.model.events.annotations.Id;
import org.l2jdd.gameserver.model.events.annotations.RegisterEvent;
import org.l2jdd.gameserver.model.events.annotations.RegisterType;
import org.l2jdd.gameserver.model.events.impl.creature.OnCreatureAttack;
import org.l2jdd.gameserver.model.events.impl.creature.OnCreatureSkillFinishCast;
import org.l2jdd.gameserver.model.events.impl.creature.npc.OnNpcSpawn;
import org.l2jdd.gameserver.model.events.listeners.ConsumerEventListener;

import ai.AbstractNpcAI;

/**
 * @author Nik
 */
public class Incarnation extends AbstractNpcAI
{
	@RegisterEvent(EventType.ON_NPC_SPAWN)
	@RegisterType(ListenerRegisterType.NPC)
	@Id(13302)
	@Id(13303)
	@Id(13304)
	@Id(13305)
	@Id(13455)
	@Id(13456)
	@Id(13457)
	@Id(13578)
	@Id(13579)
	public void onNpcSpawn(OnNpcSpawn event)
	{
		final Npc npc = event.getNpc();
		if (npc.getSummoner() != null)
		{
			npc.getSummoner().addListener(new ConsumerEventListener(npc, EventType.ON_CREATURE_ATTACK, (OnCreatureAttack e) -> onOffense(npc, e.getAttacker(), e.getTarget()), this));
			npc.getSummoner().addListener(new ConsumerEventListener(npc, EventType.ON_CREATURE_SKILL_FINISH_CAST, (OnCreatureSkillFinishCast e) -> onOffense(npc, e.getCaster(), e.getTarget()), this));
		}
	}
	
	public void onOffense(Npc npc, Creature attacker, WorldObject target)
	{
		if ((attacker == target) || (npc.getSummoner() == null))
		{
			return;
		}
		
		// Attack target of summoner
		npc.setRunning();
		npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
	}
	
	public static void main(String[] args)
	{
		new Incarnation();
	}
}
