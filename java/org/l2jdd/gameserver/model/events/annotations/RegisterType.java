
package org.l2jdd.gameserver.model.events.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.l2jdd.gameserver.model.events.ListenerRegisterType;

/**
 * @author UnAfraid
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RegisterType
{
	ListenerRegisterType value();
}
