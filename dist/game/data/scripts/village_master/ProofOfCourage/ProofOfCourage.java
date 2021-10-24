
package village_master.ProofOfCourage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.l2jdd.gameserver.data.xml.MultisellData;
import org.l2jdd.gameserver.enums.ClassId;
import org.l2jdd.gameserver.model.actor.Npc;
import org.l2jdd.gameserver.model.actor.instance.PlayerInstance;

import ai.AbstractNpcAI;

/**
 * Proof Of Courage implementation.
 * @author St3eT
 */
public class ProofOfCourage extends AbstractNpcAI
{
	// Misc
	private static final Map<Integer, List<ClassId>> CLASSLIST = new HashMap<>();
	
	static
	{
		CLASSLIST.put(32146, Arrays.asList(ClassId.TROOPER, ClassId.WARDER));
		CLASSLIST.put(32147, Arrays.asList(ClassId.ELVEN_KNIGHT, ClassId.ELVEN_SCOUT, ClassId.ELVEN_WIZARD, ClassId.ORACLE));
		CLASSLIST.put(32150, Arrays.asList(ClassId.ORC_RAIDER, ClassId.ORC_MONK));
		CLASSLIST.put(32153, Arrays.asList(ClassId.WARRIOR, ClassId.KNIGHT, ClassId.ROGUE, ClassId.WIZARD, ClassId.CLERIC));
		CLASSLIST.put(32157, Arrays.asList(ClassId.SCAVENGER, ClassId.ARTISAN));
		CLASSLIST.put(32160, Arrays.asList(ClassId.PALUS_KNIGHT, ClassId.ASSASSIN, ClassId.DARK_WIZARD, ClassId.SHILLIEN_ORACLE));
	}
	
	private ProofOfCourage()
	{
		addStartNpc(CLASSLIST.keySet());
		addTalkId(CLASSLIST.keySet());
	}
	
	@Override
	public String onTalk(Npc npc, PlayerInstance talker)
	{
		if (talker.getClassId().level() == 0)
		{
			return npc.getId() + "-noclass.html";
		}
		else if (!CLASSLIST.get(npc.getId()).contains(talker.getClassId()))
		{
			return npc.getId() + "-no.html";
		}
		MultisellData.getInstance().separateAndSend(717, talker, npc, false);
		return super.onTalk(npc, talker);
	}
	
	public static void main(String[] args)
	{
		new ProofOfCourage();
	}
}