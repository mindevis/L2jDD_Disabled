
package org.l2jdd.gameserver.network.clientpackets;

import org.l2jdd.commons.network.PacketReader;
import org.l2jdd.gameserver.data.xml.PlayerTemplateData;
import org.l2jdd.gameserver.enums.ClassId;
import org.l2jdd.gameserver.network.GameClient;
import org.l2jdd.gameserver.network.serverpackets.NewCharacterSuccess;

/**
 * @author Zoey76
 */
public class NewCharacter implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final NewCharacterSuccess ct = new NewCharacterSuccess();
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.FIGHTER)); // Human Figther
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.MAGE)); // Human Mystic
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.ELVEN_FIGHTER)); // Elven Fighter
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.ELVEN_MAGE)); // Elven Mystic
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.DARK_FIGHTER)); // Dark Fighter
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.DARK_MAGE)); // Dark Mystic
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.ORC_FIGHTER)); // Orc Fighter
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.ORC_MAGE)); // Orc Mystic
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.DWARVEN_FIGHTER)); // Dwarf Fighter
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.MALE_SOLDIER)); // Male Kamael Soldier
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.FEMALE_SOLDIER)); // Female Kamael Soldier
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.ERTHEIA_FIGHTER)); // Ertheia Fighter
		ct.addChar(PlayerTemplateData.getInstance().getTemplate(ClassId.ERTHEIA_WIZARD)); // Ertheia Wizard
		client.sendPacket(ct);
	}
}
