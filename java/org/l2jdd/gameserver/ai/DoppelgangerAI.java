
package org.l2jdd.gameserver.ai;

import static org.l2jdd.gameserver.ai.CtrlIntention.AI_INTENTION_ATTACK;
import static org.l2jdd.gameserver.ai.CtrlIntention.AI_INTENTION_FOLLOW;
import static org.l2jdd.gameserver.ai.CtrlIntention.AI_INTENTION_IDLE;

import org.l2jdd.commons.util.Rnd;
import org.l2jdd.gameserver.GameTimeController;
import org.l2jdd.gameserver.model.Location;
import org.l2jdd.gameserver.model.WorldObject;
import org.l2jdd.gameserver.model.actor.Creature;
import org.l2jdd.gameserver.model.actor.instance.DoppelgangerInstance;
import org.l2jdd.gameserver.model.items.instance.ItemInstance;
import org.l2jdd.gameserver.model.skills.Skill;
import org.l2jdd.gameserver.model.skills.SkillCaster;

public class DoppelgangerAI extends CreatureAI
{
	private volatile boolean _thinking; // to prevent recursive thinking
	private volatile boolean _startFollow;
	private Creature _lastAttack = null;
	
	public DoppelgangerAI(DoppelgangerInstance clone)
	{
		super(clone);
	}
	
	@Override
	protected void onIntentionIdle()
	{
		stopFollow();
		_startFollow = false;
		onIntentionActive();
	}
	
	@Override
	protected void onIntentionActive()
	{
		if (_startFollow)
		{
			setIntention(AI_INTENTION_FOLLOW, getActor().getSummoner());
		}
		else
		{
			super.onIntentionActive();
		}
	}
	
	private void thinkAttack()
	{
		final WorldObject target = getTarget();
		final Creature attackTarget = (target != null) && target.isCreature() ? (Creature) target : null;
		if (checkTargetLostOrDead(attackTarget))
		{
			setTarget(null);
			return;
		}
		if (maybeMoveToPawn(target, _actor.getPhysicalAttackRange()))
		{
			return;
		}
		clientStopMoving(null);
		_actor.doAutoAttack(attackTarget);
	}
	
	private void thinkCast()
	{
		if (_actor.isCastingNow(SkillCaster::isAnyNormalType))
		{
			return;
		}
		
		final WorldObject target = _skill.getTarget(_actor, _forceUse, _dontMove, false);
		if (checkTargetLost(target))
		{
			setTarget(null);
			return;
		}
		final boolean val = _startFollow;
		if (maybeMoveToPawn(target, _actor.getMagicalAttackRange(_skill)))
		{
			return;
		}
		getActor().followSummoner(false);
		setIntention(AI_INTENTION_IDLE);
		_startFollow = val;
		_actor.doCast(_skill, _item, _forceUse, _dontMove);
	}
	
	private void thinkInteract()
	{
		final WorldObject target = getTarget();
		if (checkTargetLost(target))
		{
			return;
		}
		if (maybeMoveToPawn(target, 36))
		{
			return;
		}
		setIntention(AI_INTENTION_IDLE);
	}
	
	@Override
	public void onEvtThink()
	{
		if (_thinking || _actor.isCastingNow() || _actor.isAllSkillsDisabled())
		{
			return;
		}
		_thinking = true;
		try
		{
			switch (getIntention())
			{
				case AI_INTENTION_ATTACK:
				{
					thinkAttack();
					break;
				}
				case AI_INTENTION_CAST:
				{
					thinkCast();
					break;
				}
				case AI_INTENTION_INTERACT:
				{
					thinkInteract();
					break;
				}
			}
		}
		finally
		{
			_thinking = false;
		}
	}
	
	@Override
	protected void onEvtFinishCasting()
	{
		if (_lastAttack == null)
		{
			getActor().followSummoner(_startFollow);
		}
		else
		{
			setIntention(AI_INTENTION_ATTACK, _lastAttack);
			_lastAttack = null;
		}
	}
	
	public void notifyFollowStatusChange()
	{
		_startFollow = !_startFollow;
		switch (getIntention())
		{
			case AI_INTENTION_ACTIVE:
			case AI_INTENTION_FOLLOW:
			case AI_INTENTION_IDLE:
			case AI_INTENTION_MOVE_TO:
			case AI_INTENTION_PICK_UP:
			{
				getActor().followSummoner(_startFollow);
			}
		}
	}
	
	public void setStartFollowController(boolean value)
	{
		_startFollow = value;
	}
	
	@Override
	protected void onIntentionCast(Skill skill, WorldObject target, ItemInstance item, boolean forceUse, boolean dontMove)
	{
		if (getIntention() == AI_INTENTION_ATTACK)
		{
			_lastAttack = (getTarget() != null) && getTarget().isCreature() ? (Creature) getTarget() : null;
		}
		else
		{
			_lastAttack = null;
		}
		super.onIntentionCast(skill, target, item, forceUse, dontMove);
	}
	
	@Override
	public void moveToPawn(WorldObject pawn, int offsetValue)
	{
		// Check if actor can move
		if (!_actor.isMovementDisabled() && (_actor.getMoveSpeed() > 0))
		{
			int offset = offsetValue;
			if (offset < 10)
			{
				offset = 10;
			}
			
			// prevent possible extra calls to this function (there is none?),
			// also don't send movetopawn packets too often
			boolean sendPacket = true;
			if (_clientMoving && (getTarget() == pawn))
			{
				if (_clientMovingToPawnOffset == offset)
				{
					if (GameTimeController.getInstance().getGameTicks() < _moveToPawnTimeout)
					{
						return;
					}
					sendPacket = false;
				}
				else if (_actor.isOnGeodataPath())
				{
					// minimum time to calculate new route is 2 seconds
					if (GameTimeController.getInstance().getGameTicks() < (_moveToPawnTimeout + 10))
					{
						return;
					}
				}
			}
			
			// Set AI movement data
			_clientMoving = true;
			_clientMovingToPawnOffset = offset;
			setTarget(pawn);
			_moveToPawnTimeout = GameTimeController.getInstance().getGameTicks();
			_moveToPawnTimeout += 1000 / GameTimeController.MILLIS_IN_TICK;
			if (pawn == null)
			{
				return;
			}
			
			// Calculate movement data for a move to location action and add the actor to movingObjects of GameTimeController
			// _actor.moveToLocation(pawn.getX(), pawn.getY(), pawn.getZ(), offset);
			final Location loc = new Location(pawn.getX() + Rnd.get(-offset, offset), pawn.getY() + Rnd.get(-offset, offset), pawn.getZ());
			_actor.moveToLocation(loc.getX(), loc.getY(), loc.getZ(), 0);
			if (!_actor.isMoving())
			{
				clientActionFailed();
				return;
			}
			
			// Doppelgangers always send MoveToLocation packet.
			if (sendPacket)
			{
				_actor.broadcastMoveToLocation();
			}
		}
		else
		{
			clientActionFailed();
		}
	}
	
	@Override
	public DoppelgangerInstance getActor()
	{
		return (DoppelgangerInstance) super.getActor();
	}
}
