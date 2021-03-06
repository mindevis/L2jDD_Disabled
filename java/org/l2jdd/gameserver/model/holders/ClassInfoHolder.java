
package org.l2jdd.gameserver.model.holders;

import java.util.regex.Matcher;

import org.l2jdd.gameserver.enums.ClassId;

/**
 * This class will hold the information of the player classes.
 * @author Zoey76
 */
public class ClassInfoHolder
{
	private final ClassId _classId;
	private final String _className;
	private final ClassId _parentClassId;
	
	/**
	 * Constructor for ClassInfo.
	 * @param classId the class Id.
	 * @param className the in game class name.
	 * @param parentClassId the parent class for the given {@code classId}.
	 */
	public ClassInfoHolder(ClassId classId, String className, ClassId parentClassId)
	{
		_classId = classId;
		_className = className;
		_parentClassId = parentClassId;
	}
	
	/**
	 * @return the class Id.
	 */
	public ClassId getClassId()
	{
		return _classId;
	}
	
	/**
	 * @return the hardcoded in-game class name.
	 */
	public String getClassName()
	{
		return _className;
	}
	
	/**
	 * @return the class client Id.
	 */
	@SuppressWarnings("unused")
	private int getClassClientId()
	{
		int classClientId = _classId.getId();
		if ((classClientId >= 0) && (classClientId <= 57))
		{
			classClientId += 247;
		}
		else if ((classClientId >= 88) && (classClientId <= 118))
		{
			classClientId += 1071;
		}
		else if ((classClientId >= 123) && (classClientId <= 136))
		{
			classClientId += 1438;
		}
		else if ((classClientId >= 139) && (classClientId <= 146))
		{
			classClientId += 2338;
		}
		else if ((classClientId >= 148) && (classClientId <= 181))
		{
			classClientId += 2884;
		}
		else if ((classClientId >= 182) && (classClientId <= 189))
		{
			classClientId += 3121;
		}
		return classClientId;
	}
	
	/**
	 * @return the class client Id formatted to be displayed on a HTML.
	 */
	public String getClientCode()
	{
		// TODO: Verify client ids above.
		// return "&$" + getClassClientId() + ";";
		return _className;
	}
	
	/**
	 * @return the escaped class client Id formatted to be displayed on a HTML.
	 */
	public String getEscapedClientCode()
	{
		return Matcher.quoteReplacement(getClientCode());
	}
	
	/**
	 * @return the parent class Id.
	 */
	public ClassId getParentClassId()
	{
		return _parentClassId;
	}
}
