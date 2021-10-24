
package org.l2jdd.gameserver.model.announce;

import org.l2jdd.gameserver.model.interfaces.IDeletable;
import org.l2jdd.gameserver.model.interfaces.IStorable;
import org.l2jdd.gameserver.model.interfaces.IUpdatable;

/**
 * @author UnAfraid
 */
public interface IAnnouncement extends IStorable, IUpdatable, IDeletable
{
	int getId();
	
	AnnouncementType getType();
	
	void setType(AnnouncementType type);
	
	boolean isValid();
	
	String getContent();
	
	void setContent(String content);
	
	String getAuthor();
	
	void setAuthor(String author);
}
