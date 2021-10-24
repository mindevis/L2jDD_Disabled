
package org.l2jdd.gameserver.model.interfaces;

/**
 * @author UnAfraid
 * @param <T>
 */
public interface IParameterized<T>
{
	T getParameters();
	
	void setParameters(T set);
}
